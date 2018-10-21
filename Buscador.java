import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Buscador extends Thread{

    private String fichero , ruta;
    private List<String> resultado = new ArrayList<String>();


    //Getters
    public String getFichero()
    {
        return fichero;
    }

    public String getRuta()
    {
        return ruta;
    }

    public List<String> getResultado()
    {
        return resultado;
    }

    //Setters
    public void setFichero(String fichero)
    {
        this.fichero = fichero;
    }

    public void setRuta(String ruta)
    {
        this.ruta = ruta;
    }

    //Constructores
    public Buscador(String fichero, String ruta) {
        setFichero(fichero);
        setRuta(ruta);
    }

    public Buscador() {
        this("","");
    }

    @Override
    public void run()
    {
        buscarCarpeta(new File(getRuta()), getFichero());

        int cuentaFicheros = getResultado().size();
        if(cuentaFicheros ==0)
        {
            System.err.println("\nNo se han encontrado resultados");
        }
        else
        {
            System.out.println("\nEncontrado " + cuentaFicheros + " Resultados!\n");
            /*for (String rutaFichero : getResultado())
            {
                System.out.println("Encontrado: " + rutaFichero);
            }*/
        }
    }

    public void buscarCarpeta(File carpeta, String fichero) {

        setFichero(fichero);

        ProcessBuilder pb = new ProcessBuilder("cmd", "/C","powershell (Get-FileHash "+getFichero()+").hash > hash.txt");
        try {
            Process process = pb.start();
            process.waitFor();
            if (process.exitValue() != 0)
            {
                System.err.println("Error al sacer firma hash del fichero puro");
                return;
            }
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
        catch (InterruptedException e)
        {
            System.err.println(e.getMessage());
        }

        if (carpeta.isDirectory())
        {
            buscar(carpeta);
        }
        else
        {
            System.err.println(carpeta.getAbsoluteFile() + " No es una carpeta!");
        }
    }

    private void buscar(File fichero) {

        if (fichero.isDirectory())
        {
            System.out.println("Buscando carpeta ... " + fichero.getAbsoluteFile());
            if (fichero.canRead())
            {
                for (File temp : fichero.listFiles())
                {
                    if (temp.isDirectory())
                    {
                        buscar(temp);
                    }
                    else
                    {
                        Comparador c = new Comparador(temp.getAbsoluteFile().toString());
                        c.start();
						try
                        {
                            c.join();
                        } catch (InterruptedException e)
                        {
                            System.err.println(e.getMessage());
                        }
                        resultado.add(temp.getAbsoluteFile().toString()+": "+c.getResultado());
                    }
                }
            }
            else
            {
                System.err.println(fichero.getAbsoluteFile() + "Permiso denegado");
            }
        }
    }


}
