/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;

/**
 *
 * @author SERGIO_FLORES
 */
public class TestArrayListContent {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<Integer> integers = new ArrayList<>();

        integers.add(1);
        integers.add(new Integer(2));
        integers.add((int) new Integer(3));

        System.out.println("integers.contains(1): " + integers.contains(1));
        System.out.println("integers.contains(2): " + integers.contains(2));
        System.out.println("integers.contains(3): " + integers.contains(3));
        System.out.println("integers.contains(new Integer(1)): " + integers.contains(new Integer(1)));
        System.out.println("integers.contains(new Integer(2)): " + integers.contains(new Integer(2)));
        System.out.println("integers.contains(new Integer(3)): " + integers.contains(new Integer(3)));
        System.out.println("integers.contains((int) new Integer(1)): " + integers.contains((int) new Integer(1)));
        System.out.println("integers.contains((int) new Integer(2)): " + integers.contains((int) new Integer(2)));
        System.out.println("integers.contains((int) new Integer(3)): " + integers.contains((int) new Integer(3)));
    }
}
