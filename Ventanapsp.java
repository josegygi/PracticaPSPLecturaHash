import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Ventanapsp extends JFrame implements ActionListener {

    private JLabel etiqueta0, etiqueta1;
    private JTextField texto0, texto1;
    private JPanel panel;
    private JButton boton;
    private Container contenedor;
    private GridBagConstraints constraints;

    public Ventanapsp() throws HeadlessException {
        instancias();
        initGUI();
        acciones();
    }

    private void acciones() {
        boton.addActionListener(this);
    }

    private void instancias() {
        etiqueta0 = new JLabel("Ruta Fichero Puro");
        texto0 = new JTextField();
        etiqueta1 = new JLabel("Ruta Carpeta infectada");
        texto1 = new JTextField();
        panel = new JPanel();
        constraints = new GridBagConstraints();
        boton = new JButton("Buscar");

        contenedor = getContentPane();
    }

    private void initGUI() {
        configuracionnesPanelCentral();
        setTitle("Formulario de resgistro");
        setVisible(true);
        setSize(500,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void configuracionnesPanelCentral() {

        contenedor.setLayout(new BorderLayout());
        contenedor.add(configurarPanel(),BorderLayout.CENTER);

    }

    private void configConstraints(int x, int y, int tx, int ty, double wx, double wy, int anchor, int fill, Insets i, Component c) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = tx;
        constraints.gridheight = ty;
        constraints.weightx = wx;
        constraints.weighty = wy;
        constraints.anchor = anchor;
        constraints.insets = i;
        constraints.fill = fill;
        panel.add(c,constraints);
    }

   private JPanel configurarPanel(){
       panel.setLayout(new GridBagLayout());
       configConstraints(0,0,1,1,0,0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(20,20,1,20), etiqueta0);
       configConstraints(0,1,1,1,0,0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,20,20,20), texto0);
       configConstraints(0,2,1,1,0,0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(20,20,1,20), etiqueta1);
       configConstraints(0,3,1,1,0,0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,20,20,20), texto1);
       configConstraints(0,6,1,1,0,0, GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(20,20,1,20), boton);
       return panel;
   }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == boton){
            if (texto0.getText().isEmpty() || texto1.getText().isEmpty()){
                System.err.println("Faltan datos por introducir\n");
            }
            else {
                Buscador b = new Buscador(texto0.getText(),texto1.getText());
                b.start();
				try
				{
					b.join();
				} catch (InterruptedException e1)
				{
					System.err.println(e1.getMessage());
				}
            }
        }
    }

}
