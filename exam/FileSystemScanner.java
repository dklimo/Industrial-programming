import java.io.IOException;
import java.nio.file.*;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Map;

public class FileSystemScanner {
    private final ExecutorService executor;
    private final AtomicInteger totalFiles = new AtomicInteger();
    private final AtomicLong totalSize = new AtomicLong();
    private final ConcurrentHashMap<String, AtomicInteger> extensionCounts = new ConcurrentHashMap<>();
    private final CountDownLatch latch = new CountDownLatch(1);

    public FileSystemScanner(int threadCount) {
        executor = Executors.newFixedThreadPool(threadCount);
    }

    public void scan(Path startPath) {
        if (!Files.isDirectory(startPath)) {
            System.out.println("Ошибка: путь не является директорией");
            latch.countDown();
            return;
        }
        System.out.println("Начинаем сканирование с: " + startPath.toAbsolutePath());
        try {
            executor.submit(new DirectoryTask(startPath, true));
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
            latch.countDown();
        }
    }

    public void shutdownAndPrintResults() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\nРезультаты сканирования");
        System.out.println("Всего файлов: " + totalFiles.get());
        System.out.println("Общий размер: " + totalSize.get() + " байт");
        System.out.println("Файлов по расширениям:");
        for (Map.Entry<String, AtomicInteger> entry : extensionCounts.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue().get());
        }
    }

    private class DirectoryTask implements Runnable {
        private final Path dir;
        private final boolean isRoot;

        DirectoryTask(Path dir, boolean isRoot) {
            this.dir = dir;
            this.isRoot = isRoot;
        }

        DirectoryTask(Path dir) {
            this(dir, false);
        }

        @Override
        public void run() {
            System.out.println("Поток " + Thread.currentThread().getName() + " сканирует: " + dir);
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                for (Path entry : stream) {
                    if (Files.isDirectory(entry)) {
                        executor.submit(new DirectoryTask(entry));
                    } else {
                        processFile(entry);
                    }
                }
            } catch (IOException | DirectoryIteratorException e) {
                System.err.println("Ошибка чтения директории: " + dir);
            } finally {
                if (isRoot) {
                    latch.countDown();
                }
            }
        }

        private void processFile(Path file) {
            totalFiles.incrementAndGet();
            try {
                long size = Files.size(file);
                totalSize.addAndGet(size);
            } catch (IOException e) {
                System.err.println("Ошибка чтения размера файла: " + file);
            }

            String fileName = file.getFileName().toString();
            int dotIndex = fileName.lastIndexOf('.');
            String extension = dotIndex > 0 ? fileName.substring(dotIndex).toLowerCase() : "no_extension";
            extensionCounts.computeIfAbsent(extension, k -> new AtomicInteger()).incrementAndGet();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите путь к директории: ");
        String directoryPath = scanner.nextLine().trim();

        System.out.print("Введите количество потоков: ");
        int threadCount = scanner.nextInt();

        Path startDir = Paths.get(directoryPath).toAbsolutePath();

        System.out.println("\nсканирование...");
        FileSystemScanner fsScanner = new FileSystemScanner(threadCount);
        fsScanner.scan(startDir);
        fsScanner.shutdownAndPrintResults();

        System.out.println("\nНажмите Enter для выхода...");
        scanner.nextLine();
        scanner.nextLine();
        scanner.close();
    }
}