package com.utube;

import java.util.ArrayList;
import java.util.List;

import static com.utube.DataProcessor.dataList;

class DataProcessor {
    public static List<String> dataList = new ArrayList<>();

    public synchronized boolean doesDataExist(String data) {
        return dataList.contains(data);
    }

    public synchronized void insertDataIfNotExists(String data) {
        if (!dataList.contains(data)) {
            dataList.add(data);
            System.out.println("Data inserted: " + data);
        } else {
            System.out.println("Data already exists: " + data);
        }
    }

}

class DataManipulationThread extends Thread {
    private DataProcessor dataProcessor;
    private String data;

    public DataManipulationThread(DataProcessor dataProcessor, String data) {
        this.dataProcessor = dataProcessor;
        this.data = data;
    }

    @Override
    public void run() {
        // Check if data exists
        boolean dataExists = dataProcessor.doesDataExist(data);
        System.out.println("Thread " + Thread.currentThread().getId() + " - Does data exist: " + dataExists);

        // Insert data if it doesn't exist
        dataProcessor.insertDataIfNotExists(data);
    }
}

class SynchronizationExample {
    public static void main(String[] args) {
        DataProcessor dataProcessor = new DataProcessor();

        // Create multiple threads that manipulate the data
        DataManipulationThread thread1 = new DataManipulationThread(dataProcessor, "apple");
        DataManipulationThread thread2 = new DataManipulationThread(dataProcessor, "banana");
        DataManipulationThread thread3 = new DataManipulationThread(dataProcessor, "apple");

        // Start the threads
        thread1.start();
        thread2.start();
        thread3.start();

        // Wait for threads to finish
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Perform any final operations
        System.out.println("Final data list: ");
        dataList.forEach(System.out::println);
    }
}
