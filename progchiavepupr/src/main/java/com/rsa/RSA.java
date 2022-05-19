package com.rsa;

public class RSA
{
    private int z;
    private static final char[] alfabeto = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'}; 

    public RSA ()
    {

    }

    public int[] ChiavePubblica(int p, int q)
        {
            int chiavePubblica[] = new int[2];
            int n = 0;
            int e = 2;

            this.z = (p - 1) * (q - 1);
            n = p * q;
            e = MCD(e,z);

            chiavePubblica[0] = e;
            chiavePubblica[1] = n;

            return chiavePubblica;
        }

        public int MCD(int e, int z)
        {
            int i = e; 
            int j = z;
            int resto;

            do
            {
                while ((resto = i % j) != 0)
                    {
                        i = j;
                        j = resto;
                    }
                
                    if(j != 1)
                    {
                        e++;
                        i = e;
                        j = z;
                    }

            } while(j != 1);

            return e;
        }

        public int[] ChiavePrivata(int e, int n)
        {
            int d = TrovaD(this.z, e);
            int chiavePrivata[]= new int[2];

            chiavePrivata[0] = d;
            chiavePrivata[1] = n;

            return chiavePrivata;
        }

        public int TrovaD(int z, int e)
        {
            int d = 0;

            for(int k = 1; ; k++)
            {
                if((k * z + 1) % e == 0)
                {
                    d = (k * z + 1) / e;
                    break;
                }
            }

            return d;
        }

        public String Cifra(String messaggio, int e, int n)
        {
            String messaggioCifrato = "";
            long z;

            for(int i = 0; i < messaggio.length(); i++)
            {
                z =  (long) (Math.pow(getIndice(messaggio.charAt(i)), e) % n);

                messaggioCifrato += z + ";";
            }

            return messaggioCifrato;
        }

        public String Decifra(String messaggio, int d, int n)
        {
            String messaggioDecifrato = "";
            String[] messaggioCifrato = messaggio.split(";");
            long z;

            for(int i = 0; i < messaggioCifrato.length; i++)
            {

                z = (long) (Math.pow(Integer.valueOf(messaggioCifrato[i]), d) % n);

                messaggioDecifrato += getLettera(z);
            }

            return messaggioDecifrato;
        }

    public char getLettera(long i)
    {
        return alfabeto[(int) i];
    }

    public int getIndice(char c)
    {
        for(int i = 0; i < alfabeto.length; i++)
            {
                if(alfabeto[i] == c)
                {
                    return i;
                }
            }

        return 0;
    }
}