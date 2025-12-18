import java.io.*;
import java.util.*;

public class DrugIO {
    private final String filename;
    private RandomAccessFile raf;
    private NavigableMap<String, Long> index = new TreeMap<>();
    private String currentIndexField;

    public DrugIO(String filename) throws FileNotFoundException {
        this.filename = filename;
        this.raf = new RandomAccessFile(filename, "rw");
        this.currentIndexField = "drugstoreNum";
    }
    public String getCurrentIndexField() {
        return currentIndexField;
    }
    public void fillTestData(List<Drug> drugs) throws IOException {
        raf.setLength(0);
        for(Drug drug: drugs){
            long pos = raf.getFilePointer();
            writeDrug(drug);
            index.put(drug.getDrugstoreNum(), pos);
        }
    }
    private void writeDrug(Drug drug) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(drug);
        oos.flush();
        byte[] data = bos.toByteArray();
        raf.writeInt(data.length);
        raf.write(data);
    }
    private Drug readDrug() throws IOException, ClassNotFoundException {
        int length = raf.readInt();
        byte[] data = new byte[length];
        raf.readFully(data);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        return (Drug) ois.readObject();
    }
    public void printAllDrugInfo() throws IOException, ClassNotFoundException {
        System.out.printf("%-12s | %-15s | %-8s | %-8s | %-12s | %-12s%n",
                "Drugstore", "Drug Name", "Packages", "Price", "Receipt Date", "Expiry Date");
        System.out.println("-------------------------------------------------------------------------------");
        raf.seek(0);
        while(raf.getFilePointer() < raf.length()){
            Drug drug = readDrug();
            System.out.println(drug);
        }

    }
    public void buildIndexByField(String field) throws IOException, ClassNotFoundException {
        index.clear();
        raf.seek(0);
        currentIndexField = field;
        while (raf.getFilePointer() < raf.length()) {
            long pos = raf.getFilePointer();
            Drug d = readDrug();
            String key;
            switch (field) {
                case "drugstoreNum": key = d.getDrugstoreNum(); break;
                case "drugName": key = d.getDrugName(); break;
                case "receiptDate": key = d.getReceiptDate(); break;
                case "expirationDate": key = d.getExpirationDate(); break;
                default: throw new IllegalArgumentException("Unknown index field");
            }
            index.put(key, pos);
        }
    }
    public void printByIndex(boolean ascending) throws IOException, ClassNotFoundException {
        System.out.printf("%-12s | %-15s | %-8s | %-8s | %-12s | %-12s%n",
                "Drugstore", "Drug Name", "Packages", "Price", "Receipt Date", "Expiry Date");
        System.out.println("-------------------------------------------------------------------------------");
        NavigableMap<String, Long> navMap;
        if (ascending) {
            navMap = index;
        } else {
            navMap = index.descendingMap();
        }

        for (Map.Entry<String, Long> e : navMap.entrySet()) {
            raf.seek(e.getValue());
            Drug d = readDrug();
            System.out.println(d);
        }
    }

public void findEqual(String key) throws IOException, ClassNotFoundException {
        if(index.containsKey(key)){
            System.out.printf("%-12s | %-15s | %-8s | %-8s | %-12s | %-12s%n",
                    "Drugstore", "Drug Name", "Packages", "Price", "Receipt Date", "Expiry Date");
            System.out.println("-------------------------------------------------------------------------------");
            raf.seek(index.get(key));
            System.out.println(readDrug());
        }
        else{
            System.out.println("Drug not found");
        }
}
public void findGreater(String key)throws IOException, ClassNotFoundException{
    System.out.printf("%-12s | %-15s | %-8s | %-8s | %-12s | %-12s%n",
            "Drugstore", "Drug Name", "Packages", "Price", "Receipt Date", "Expiry Date");
    System.out.println("-------------------------------------------------------------------------------");
        for(String k : index.tailMap(key, false).keySet()){
            raf.seek(index.get(k));
            System.out.println(readDrug());
        }
}
    public void findLess(String key)throws IOException, ClassNotFoundException{
        System.out.printf("%-12s | %-15s | %-8s | %-8s | %-12s | %-12s%n",
                "Drugstore", "Drug Name", "Packages", "Price", "Receipt Date", "Expiry Date");
        System.out.println("-------------------------------------------------------------------------------");
        for(String k : index.headMap(key, false).keySet()){
            raf.seek(index.get(k));
            System.out.println(readDrug());
        }
    }
    public void deleteByIndex(String key){
        if(index.remove(key)!= null){
            System.out.println("Delete from index: " + key);
        }
        else{
            System.out.println("Key does not exist");
        }
    }
}
