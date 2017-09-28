/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package copybytesimaxe;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author otorradomiguez
 */
public class Copybytesimaxe {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        File imaxeOrixinal = new File("/home/local/DANIELCASTELAO/otorradomiguez/NetBeansProjects/copybytesimaxe/foto1.jpg");
        File imaxeCopia = new File("/home/local/DANIELCASTELAO/otorradomiguez/NetBeansProjects/copybytesimaxe/foto2.jpg");
        File imaxeCopia2 = new File("/home/local/DANIELCASTELAO/otorradomiguez/NetBeansProjects/copybytesimaxe/foto3.jpg");
        //Copiamos la imagen
        copiarArchivo(imaxeOrixinal, imaxeCopia, false);
        /* Copiamos los bytes de la imagen al final de la copia
        El archivo resultante sigue mostrando la imagen correctamente, pero ocupa
        el doble, ya que contiene la imagen 2 veces */
        copiarArchivo(imaxeOrixinal, imaxeCopia, true);
        //Copiamos usando bufferedStream (pasa de tardar unos 22-24ms a 1-2ms en este caso)
        //copiarArchivoBuffered(imaxeOrixinal,imaxeCopia2,false);
        leerArchivo(imaxeCopia);
    }

    public static void copiarArchivo(File archivo, File archivoNuevo, boolean añadir) {
        int byteActual = 0;
        long timeStart, timeEnd;
        try {
            FileInputStream fInput = new FileInputStream(archivo);
            FileOutputStream fOutput = new FileOutputStream(archivoNuevo, añadir);
            timeStart = System.currentTimeMillis();
            while (byteActual != -1) {
                byteActual = fInput.read();
                if (byteActual != -1) {
                    fOutput.write(byteActual);
                } else {
                    fOutput.close();
                    fInput.close();
                }
            }
            timeEnd = System.currentTimeMillis();
            System.out.println("Archivo copiado en " + (timeEnd - timeStart) + "ms");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Copybytesimaxe.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Copybytesimaxe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void copiarArchivoBuffered(File archivo, File archivoNuevo, boolean añadir) {
        int byteActual = 0;
        long timeStart, timeEnd;
        try {
            FileInputStream fInput = new FileInputStream(archivo);
            FileOutputStream fOutput = new FileOutputStream(archivoNuevo, añadir);
            BufferedInputStream bInput = new BufferedInputStream(fInput);
            BufferedOutputStream bOutput = new BufferedOutputStream(fOutput);
            timeStart = System.currentTimeMillis();
            while (byteActual != -1) {
                byteActual = bInput.read();
                if (byteActual != -1) {
                    bOutput.write(byteActual);
                } else {
                    bOutput.close();
                    bInput.close();
                    fOutput.close();
                    fInput.close();
                }
            }
            timeEnd = System.currentTimeMillis();
            System.out.println("Archivo copiado en " + (timeEnd - timeStart) + "ms con Buffered I/O Stream");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Copybytesimaxe.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Copybytesimaxe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Lee el archivo enviado, busca el header(FF D8) de jpeg y crea un archivo con los datos contenidos en él
     * hasta que llega al final(FF D9), tras lo cual busca si hay un nuevo header jpeg y en caso de encontrarlo 
     * crea un archivo nuevo
     * @param archivo 
     */
    public static void leerArchivo(File archivo) {
        int byteActual = 0;
        int byteAnterior = -1;
        long timeStart, timeEnd;
        int cantidadDeImagenes = 0;
        try {
            FileInputStream fInput = new FileInputStream(archivo);
            FileOutputStream fOutput = new FileOutputStream("/home/local/DANIELCASTELAO/otorradomiguez/NetBeansProjects/copybytesimaxe/fotoUnida" + cantidadDeImagenes + ".jpg", false);
            timeStart = System.currentTimeMillis();
            while (byteActual != -1) {
                byteAnterior = byteActual;
                byteActual = fInput.read();
                //255-217 marcan el final del jpeg
                if (byteAnterior == 255 && byteActual == 217) {
                    fOutput.write(byteActual);
                    byteAnterior = -1;
                    fOutput.close();
                    cantidadDeImagenes++;
                    System.out.println("Termina este jpeg");
                } //255-216 marcan el principio del jpeg
                else if (byteAnterior == 255 && byteActual == 216) {
                    System.out.println("Empieza una nueva imagen");
                    fOutput.close();
                    fOutput = new FileOutputStream("/home/local/DANIELCASTELAO/otorradomiguez/NetBeansProjects/copybytesimaxe/fotoUnida" + cantidadDeImagenes + ".jpg", false);
                    fOutput.write(byteAnterior);
                    fOutput.write(byteActual);
                } else {
                    try {
                        fOutput.write(byteActual);
                    } catch (Exception ex) {
                        System.out.println("Este byte no se escribio");
                    }
                }
            }
            fInput.close();
            timeEnd = System.currentTimeMillis();
            System.out.println("Archivo copiado en " + (timeEnd - timeStart) + "ms");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Copybytesimaxe.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Copybytesimaxe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
