import java.util.*;

public class Main {
    public static void main(String[] args) {

        //ex 1
        int nr = 5;
        boolean rezultat = nrPrim(nr);
        System.out.println("Ex 1:");
        System.out.println(rezultat);

        //ex 2
        //cel mai mare divizor comun while
        int a = 2;
        int b = 10;
        int rezultat2 = CMMDC(a, b);
        System.out.println();
        System.out.println("Ex 2:");
        System.out.println(rezultat2);

        //ex 3
        String sir = "abcd";
        String rezultat3 = jumateSir(sir);
        System.out.println();
        System.out.println("Ex 3:");
        System.out.println(rezultat3);

        //ex 4
        List<Integer> lista = List.of(1,2,3,4);
        int n = 3;
        List<Integer> rezultat4 = primeleN(lista, n);
        System.out.println();
        System.out.println("Ex: 4");
        System.out.println(rezultat4);

        //ex 5
        int numar = 53;
        int rezultat5 = sumaCifrelor(numar);
        System.out.println();
        System.out.println("Ex: 5");
        System.out.println(rezultat5);
    }

    private static int sumaCifrelor(int numar) {

        int suma = 0;
        while(numar != 0)
        {
            suma += numar % 10;
            numar /= 10;
        }
        return suma;
    }

    private static List<Integer> primeleN(List<Integer> lista, int n) {

        List<Integer> rez = new ArrayList<>();
        for(int i=0; i<n; i++)
        {
            rez.add(lista.get(i));
        }
        return rez;
    }

    private static String jumateSir(String sir) {

        String rez = "";
        for(int i=0; i<sir.length()/2; i++)
        {
            rez += sir.charAt(i);
        }
        return rez;
    }

    private static int CMMDC(int a, int b) {

        while (b != 0)
        {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private static boolean nrPrim(int nr) {

        int contor = 0;
        for(int i=1; i<=nr; i++)
        {
            if(nr % i == 0)
            {
                contor++;
            }
        }
        if(contor == 2 || contor == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}