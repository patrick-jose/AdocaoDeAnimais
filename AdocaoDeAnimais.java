import static java.awt.EventQueue.invokeLater;

public class AdocaoDeAnimais {
    public static void main(String[] args) {
        invokeLater(new Runnable() {
            public void run() {            
                new Janela().setVisible(true);
            }
        });
    }  
}
