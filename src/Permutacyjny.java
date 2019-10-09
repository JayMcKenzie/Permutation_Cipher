import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.lang.*;
import java.util.Map.*;


public class Permutacyjny {

    private static void random(int tab[])
    {
        Random rnd = ThreadLocalRandom.current();
        for (int i = tab.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = tab[index];
            tab[index] = tab[i];
            tab[i] = a;
        }
    }

    private static void fill(int columns, int key_length, int number_of_signs, char tab[][], char signs_tab[])
    {
        int k = 0;
        for (int i=0; i<columns;i++)
        {
            for (int j=0; j<key_length; j++)
            {
                if(k < number_of_signs) {
                    if(signs_tab[k] == ' ')
                    {
                        tab[i][j] = '_';
                    }
                    else tab[i][j] = signs_tab[k];
                    k++;
                }
                else
                    tab[i][j]='_';
            }
        }
    }

    public static void fill_encyphered(int columns, int key_length, int number_of_signs, char tab[][], char signs_tab[])
    {
        int k = 0;
        for (int i=0; i<columns;i++)
        {
            for (int j=0; j<key_length; j++)
            {
                if(k < number_of_signs) {
                    if(signs_tab[k] == '_')
                    {
                        tab[i][j] = ' ';
                    }
                    else tab[i][j] = signs_tab[k];
                    k++;
                }
                else
                    tab[i][j]=' ';
            }
        }
    }

    private static void write (char tab[][], int columns, int key_length)
    {
        for (int i=0; i<columns; i++)
        {
            for(int j=0; j<key_length; j++)
            {
                System.out.print(tab[i][j] + " ");
            }
            System.out.print("\n");
        }
    }

    private static StringBuilder cipher (int columns, int key_length, char tab[][], int key_array[], char ciphered_tab[][])
    {
        StringBuilder ciphered = new StringBuilder("");
        for (int i=0; i<columns; i++)
        {
            for(int j=0; j<key_length;j++)
            {
                ciphered_tab[i][j] = tab[i][key_array[j]-1];
            }
        }
        for (int i =0; i<columns; i++)
        {
            for(int j=0; j<key_length; j++)
            {
                ciphered.append(ciphered_tab[i][j]);
            }
        }
        return ciphered;
    }

    private static StringBuilder encipher (int columns, int key_length, char tab[][], int key_array[], char enciphered_tab[][])
    {
        StringBuilder enciphered = new StringBuilder("");
        for (int i=0; i<columns; i++)
        {
            for(int j=0; j<key_length;j++)
            {
                enciphered_tab[i][j] = tab[i][key_array[j]-1];
            }
        }
        for (int i = 0; i<columns; i++)
        {
            for (int j = 0; j<key_length; j++)
            {
                if(enciphered_tab[i][j] == '_')
                    enciphered_tab[i][j] = ' ';
            }
        }
        for (int i =0; i<columns; i++)
        {
            for(int j=0; j<key_length; j++)
            {
                enciphered.append(enciphered_tab[i][j]);
            }
        }
        return enciphered;
    }

    private static <K, V extends Comparable<? super V>> Map sort(Map<Integer,Integer> reversed)
    {
        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(reversed.entrySet());
        list.sort(Entry.comparingByValue());

        Map<Integer, Integer> result = new LinkedHashMap<>();
        for (Entry<Integer,Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    private static Map<Integer,Integer> reverse_key(int[] key_array)
    {
        Map<Integer,Integer> reversed = new HashMap<>();
        for(int i=0; i<key_array.length;i++)
        {
            reversed.put(i+1,key_array[i]);
        }
        Map<Integer, Integer> reversed_final = new HashMap<>();
        reversed_final = sort(reversed);
        return reversed_final;
    }

    private static int[] reverse_array(List<Integer> reversed_key_list) {
        int[] reversed_key_array = new int[reversed_key_list.size()];
        for( int i = 0; i<reversed_key_list.size(); i++)
        {
            reversed_key_array[i] = reversed_key_list.get(i);
        }
        return reversed_key_array;
    }

    private static boolean isStringInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private static void pobieranie_z_pliku() throws FileNotFoundException {
        int key_length;                     //dlugosc klucza
        System.out.println("Podaj dlugosc klucza: ");
        Scanner scan = new Scanner(System.in);
        String klucz = scan.nextLine();
        key_length = Integer.parseInt(klucz);
        int[] key_array = new int[key_length];
        for (int i = 0; i < key_length; i++) {
            key_array[i] = i + 1;
        }
        random(key_array);
        Scanner input = new Scanner(new File("lab1.txt"));
        String text = input.nextLine();
        int number_of_signs = text.length();
        char[] signs_tab = new char[text.length()];
        for (int i = 0; i < text.length(); i++) {
            signs_tab[i] = text.charAt(i);
        }

        int columns = (int) Math.ceil(((float) number_of_signs / key_length));
        char[][] tab = new char[columns][key_length];
        fill(columns, key_length, number_of_signs, tab, signs_tab);
        System.out.print("\nTablica tekstu: \n");
        write(tab, columns, key_length);
        StringBuilder CypheredText;
        char[][] ciphered_tab = new char[columns][key_length];
        CypheredText = cipher(columns, key_length, tab, key_array, ciphered_tab);
        System.out.print("\nZaszyfrowana tablica: \n");
        write(ciphered_tab, columns, key_length);
        System.out.print("\n" + CypheredText);
        PrintWriter output = new PrintWriter("crypted.txt");
        output.println(CypheredText);
        Map<Integer, Integer> reversed_key;
        reversed_key = reverse_key(key_array);
        //System.out.print(reversed_key.entrySet());
        List<Integer> reversed_key_list = new ArrayList<>(reversed_key.keySet());
        int[] reversed_array;
        reversed_array = reverse_array(reversed_key_list);
        StringBuilder EncypheredText;
        char[][] enciphered_tab = new char[columns][key_length];
        EncypheredText = encipher(columns, key_length, ciphered_tab, reversed_array, enciphered_tab);
        System.out.print("\nOdszyfrowana tablica: \n");
        write(enciphered_tab, columns, key_length);
        System.out.print("\n" + EncypheredText);
        output.close();
        input.close();
    }

    private static void wpisywanie_tekstu()
    {
        int key_length;                     //dlugosc klucza
        System.out.println("Podaj dlugosc klucza: ");
        Scanner scan = new Scanner(System.in);
        String klucz = scan.nextLine();
        key_length = Integer.parseInt(klucz);
        int[] key_array = new int[key_length];
        for (int i = 0; i < key_length; i++) {
            key_array[i] = i + 1;
        }
        random(key_array);
        System.out.println("\nPodaj tekst: ");
        Scanner text = new Scanner(System.in);
        String text2 = text.nextLine();
        int number_of_signs = text2.length();
        char[] signs_tab = new char[text2.length()];
        for (int i = 0; i < text2.length(); i++) {
            signs_tab[i] = text2.charAt(i);
        }

        int columns = (int) Math.ceil(((float) number_of_signs / key_length));
        char[][] tab = new char[columns][key_length];
        fill(columns, key_length, number_of_signs, tab, signs_tab);
        System.out.print("\nTablica tekstu: \n");
        write(tab, columns, key_length);
        StringBuilder CypheredText;
        char[][] ciphered_tab = new char[columns][key_length];
        CypheredText = cipher(columns, key_length, tab, key_array, ciphered_tab);
        System.out.print("\nZaszyfrowana tablica: \n");
        write(ciphered_tab, columns, key_length);
        System.out.print("\n" + CypheredText);
        Map<Integer, Integer> reversed_key;
        reversed_key = reverse_key(key_array);
        //System.out.print(reversed_key.entrySet());
        List<Integer> reversed_key_list = new ArrayList<>(reversed_key.keySet());
        int[] reversed_array;
        reversed_array = reverse_array(reversed_key_list);
        StringBuilder EncypheredText;
        char[][] enciphered_tab = new char[columns][key_length];
        EncypheredText = encipher(columns, key_length, ciphered_tab, reversed_array, enciphered_tab);
        System.out.print("\nOdszyfrowana tablica: \n");
        write(enciphered_tab, columns, key_length);
        System.out.print("\n" + EncypheredText);
    }

    public static void main (String args[]) throws IOException{
        char answer;
        do {
            int wybor;
                do
                {
                    System.out.println("Wybierz opcje:\n1: Pobieranie tekstu z pliku\n2: Wpisywanie tekstu manualnie");
                    Scanner scan = new Scanner(System.in);
                    String choice = scan.nextLine();
                    if(isStringInt(choice))
                    wybor = Integer.parseInt(choice);
                    else {
                        System.out.print("\nPodano bledny znak.");
                        break;
                    }
                switch (wybor) {
                    case 1:
                        pobieranie_z_pliku();
                        break;
                    case 2:
                        wpisywanie_tekstu();
                        break;
                    default:
                        System.out.print("\nBlad, prosze podac liczbe 1 lub 2\n");
                }
            }
            while (wybor != 1 && wybor != 2 );
            do {
                System.out.print("\nCzy chcesz wykonac algorytm jeszcze raz? t/n");
                Scanner ans = new Scanner(System.in);
                answer = ans.next().charAt(0);
            }
            while(answer != 't' && answer != 'n' );
        }
        while(answer == 't');

    }
}
