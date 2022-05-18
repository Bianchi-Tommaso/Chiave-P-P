package com.rsa;

public class RSA
{
    private int z;

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
            char c;
            int ascii = 0;
            int z;

            for(int i = 0; i < messaggio.length(); i++)
            {
                c = messaggio.charAt(i);
                ascii = (int) c;

                z =  (int) (Math.pow(ascii, e) % n);

                messaggioCifrato += z + ";";
            }

            return messaggioCifrato;
        }

        public String Decifra(String messaggio, int d, int n)
    {
        String messaggioDecifrato = "";
        String[] messaggioCifrato = messaggio.split(";");
        char c;
        int z;

        for(int i = 0; i < messaggioCifrato.length; i++)
        {

            z =  (int) Math.pow(Integer.valueOf(messaggioCifrato[i]), d) % n;
            c = (char) z;

            messaggioDecifrato += c;
        }

        return messaggioDecifrato;
    }
}