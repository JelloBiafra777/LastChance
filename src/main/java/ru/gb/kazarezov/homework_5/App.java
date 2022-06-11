package ru.gb.kazarezov.homework_5;


public class App {

    static final int ARRAY_SIZE = 10000000;

    public static void main(String[] args) {

        System.out.println(directCalculation());
        System.out.println(threatCalculation());

    }

    public static void initiateArray(float[] array) {

        for (int i = 0; i < array.length; i++) {
            array[i] = 1;
        }

    }

    public static float directCalculation() {

        float[] arr = new float[ARRAY_SIZE];
        initiateArray(arr);
        long a = System.currentTimeMillis();

        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        return (System.currentTimeMillis() - a);
    }

    public static float threatCalculation() {

        float[] arr = new float[ARRAY_SIZE];
        float[] arr1 = new float[ARRAY_SIZE / 2];
        float[] arr2 = new float[ARRAY_SIZE / 2];

        initiateArray(arr);
        long a = System.currentTimeMillis();

        System.arraycopy(arr, 0, arr1, 0, ARRAY_SIZE / 2);
        System.arraycopy(arr, ARRAY_SIZE / 2 - 1, arr2, 0, ARRAY_SIZE / 2);


        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < arr1.length; i++) {
                arr1[i] = (float) (arr1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < arr2.length; i++) {
                int j = i + ARRAY_SIZE / 2;
                arr2[i] = (float) (arr2[i] * Math.sin(0.2f + j / 5) * Math.cos(0.2f + j / 5) * Math.cos(0.4f + j / 2));
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        System.arraycopy(arr1, 0, arr, 0, ARRAY_SIZE / 2);
        System.arraycopy(arr2, 0, arr, ARRAY_SIZE / 2, ARRAY_SIZE / 2);

        return (System.currentTimeMillis() - a);
    }


}
