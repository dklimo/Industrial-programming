import java.util.*;

public class SubjectIndex {
    public static final int maxPages = 100;
    private class Component{
        private String word;
        private Set<Integer> pageIndices;

        public Component(String word){
            if(word == null || word.trim().isEmpty()){
                throw new IllegalArgumentException("Word can't be empty!");
            }
            this.word = word;
            this.pageIndices = new TreeSet<>();
        }

        public String getWord() {
            return word;
        }
        public Set<Integer> GetPages(){
            return new TreeSet<>(pageIndices);
        }
        public boolean AddPageIndex(int pageIndex){
            if(pageIndex <= 0){
                throw new IllegalArgumentException("Index of page should be positive!");
            }
            if(pageIndex >= maxPages){
                throw new IllegalStateException("Maximum number of page numbers reached: " + maxPages);
            }
            return pageIndices.add(pageIndex);
        }
        public boolean RemovePageIndex(int pageIndex){
            return pageIndices.remove(pageIndex);
        }
        public void CleanPageIndices(){
            pageIndices.clear();
        }
        @Override
        public String toString() {
            return word + " -> " + pageIndices;
        }
    }
    private Map<String, Component> subject;
    public SubjectIndex() {
        subject = new HashMap<>();
    }
    public boolean addComponent(String word) {
        if (!subject.containsKey(word)) {
           subject.put(word, new Component(word));
            return true;
        }
        return false;
    }
    public boolean removeComponent(String word) {
        return subject.remove(word) != null;
    }
    public boolean AddPageToWord(String word, int pageIndex){
        Component comp = subject.get(word);
        if(comp != null){
            return comp.AddPageIndex(pageIndex);
        }
        return false;
    }
    public boolean RemovePageFromWord(String word, int pageIndex){
        Component comp = subject.get(word);
        if(comp != null){
            return comp.RemovePageIndex(pageIndex);
        }
        return false;
    }
    public Component GetComponent(String word){
        return subject.get(word);
    }
    public Set<String> getAllWords() {
        return new TreeSet<>(subject.keySet());
    }
    public void printIndex() {
        for (Component comp : subject.values()) {
            System.out.println(comp);
        }
    }
}
