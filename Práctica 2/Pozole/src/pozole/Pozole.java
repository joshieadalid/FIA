/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pozole;

public class Pozole {
    public static void main(String[] args) {
        int[] initialState = new int[]{2, 8, 3, 1, 6, 4, 7, 0, 5};
        int[] finalState = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] size = new int[]{3, 3};
        new Tablero(initialState, finalState, size).setVisible(true);
    }
}
