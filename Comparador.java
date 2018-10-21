import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Comparador extends Thread{
    private File hashPrincipal = new File("hash.txt");
    private File hashTemp = new File("hashtemp.txt");
    private String ficheroSospechoso;
    private String resultado;

    public String getFicheroSospechoso()
    {
        return ficheroSospechoso;
    }

    public String getResultado()
    {
        return resultado;
    }

    public void setFicheroSospechoso(String ficheroSospechoso)
    {
        this.ficheroSospechoso = ficheroSospechoso;
    }

    public void setResultado(String resultado)
    {
        this.resultado = resultado;
    }

    public Comparador(String ficheroSospechoso) 
    {
        setFicheroSospechoso(ficheroSospechoso);
    }

    @Override
    public void run() 
    {
        ProcessBuilder pb = new ProcessBuilder("cmd", "/C","powershell (Get-FileHash "+getFicheroSospechoso()+").hash > hashtemp.txt");
        try
        {
            Process process = pb.start();
            process.waitFor();
            if (process.exitValue() != 0)
            {
                System.err.println("Error al sacer firma hash del fichero infectado");
                return;
            }
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }

        compararHash();

    }

    private String compararHash()
    {

        String salida = "";
        FileReader fr = null, fr2 = null;
        BufferedReader br = null, br2 = null;

        try
        {
            fr = new FileReader(hashPrincipal);
            br = new BufferedReader(fr);
            fr2 = new FileReader(hashTemp);
            br2 = new BufferedReader(fr2);

            String linea1 = br.readLine();
            String linea2 = br2.readLine();

            if (linea1.equals(linea2))
                System.out.println(getFicheroSospechoso()+": Firma hash equivalente");
            else
                System.out.println(getFicheroSospechoso()+": Firma hash diferente");

        }
        catch (FileNotFoundException e)
        {
            System.err.println(e.getMessage());
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            try
            {
                br.close();
                fr.close();
                br2.close();
                fr2.close();
            }
            catch (IOException e)
            {
                System.err.println(e.getMessage());
            }
        }
        return salida;
    }
}
