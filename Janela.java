import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class Janela extends JFrame {
    private JButton btnConfirmar;
    private JButton btnListar;
    private JButton btnNovo;
    private JButton btnExcluir;
    private JScrollPane jScrollPanel;
    private JTable tbTabela;
    private JPanel jPanelDono;
    private JLabel lblAdocao;
    private JLabel lblEndereco;
    private JLabel lblNome;
    private JLabel JHiddenLabel;
    private JTextField txtAdocao;
    private JTextField txtEndereco;
    private JTextField txtNome;
    
    public Janela() {
        TreeMap<Animal, Integer> map = new TreeMap<Animal, Integer>(new AnimalComp());
        map = lerDados(map);
        
        JFrame frame = new JFrame();
        
        Set<Animal> keys = map.keySet();
        ArrayList<Animal> animais = new ArrayList<Animal>();
       
        for (Animal animal: keys) {
            animais.add(animal);
        }        

        String col[] = { "Nome", "Tipo", "Chegada", "Idade", "Peso", "", "", "", "" };
        DefaultTableModel tableModel = new DefaultTableModel(col, 0);
        
        for (int i = 0; i < animais.size(); i++)
            tableModel.addRow(animais.get(i).retornaArray());
        
        setTitle("Adoção de Animais (Beta)");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(null, "teste");
            }
        });
        
        initComponents(tableModel, animais);
    }                

    private void initComponents(DefaultTableModel tbm, ArrayList<Animal> animais) {
        jScrollPanel = new JScrollPane();
        tbTabela = new JTable();
        btnConfirmar = new JButton();
        btnExcluir = new JButton();
        btnNovo = new JButton();
        btnListar = new JButton();
        jPanelDono = new JPanel();
        lblNome = new JLabel();
        lblAdocao = new JLabel();
        lblEndereco = new JLabel();
        JHiddenLabel = new JLabel();
        txtNome = new JTextField();
        txtAdocao = new JTextField();
        txtEndereco = new JTextField();    

        tbTabela.setModel(tbm);
        TableColumnModel tcm = tbTabela.getColumnModel();
        tcm.removeColumn(tcm.getColumn(6));
        tcm.removeColumn(tcm.getColumn(6));
        tcm.removeColumn(tcm.getColumn(6));
        tbTabela.setAutoCreateRowSorter(true);
        jScrollPanel.setViewportView(tbTabela);
        tbTabela.setVisible(false);
        JHiddenLabel.setVisible(false);
        
        btnConfirmar.setBackground(new java.awt.Color(51, 204, 255));
        btnConfirmar.setText("Confirmar");

        btnExcluir.setBackground(new java.awt.Color(51, 204, 255));
        btnExcluir.setText("Excluir");

        btnNovo.setBackground(new java.awt.Color(51, 204, 255));
        btnNovo.setText("Novo");

        btnListar.setBackground(new java.awt.Color(51, 204, 255));
        btnListar.setText("Listar");
        
        jPanelDono.setVisible(false);
        
        lblNome.setText("Nome");
        lblAdocao.setText("Data Adoção");
        lblEndereco.setText("Endereço");

        txtNome.setText("");
        txtAdocao.setText("");
        txtEndereco.setText("");
        
        Action openAdocao = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                int modelRow = Integer.valueOf( e.getActionCommand() );
                Dono dono = new Dono(animais.get(modelRow).getNomeDono(), animais.get(modelRow).getAdocao(), animais.get(modelRow).getEndereco());
                String dataFormatada = dono.getAdocao().format(dtf);
                
                txtNome.setText(dono.getNome());
                if (!dono.getAdocao().equals(LocalDate.MIN))
                    txtAdocao.setText(dataFormatada);
                else
                    txtAdocao.setText("");
                txtEndereco.setText(dono.getEndereco());
                
                jPanelDono.setVisible(true);
                JHiddenLabel.setText(String.valueOf(modelRow));
            }
        };

        ButtonColumn buttonColumn = new ButtonColumn(tbTabela, openAdocao, 5);
        buttonColumn.setMnemonic(KeyEvent.VK_D);
        
        btnListar.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                tbTabela.setVisible(true);
            }
        });
        
        btnNovo.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                JTable table = tbTabela;
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.addRow(new Object[]{"", "", "", "", "", ""});
            }
        });
        
        btnConfirmar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JTable table = tbTabela;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                Dono dono = new Dono();
                
                if(!(txtNome.getText().equals("")) && !(txtNome.getText().equals(" "))){
                    dono = new Dono(txtNome.getText(), LocalDate.parse(txtAdocao.getText(), dtf), txtEndereco.getText());
                }
                
                jPanelDono.setVisible(false);
                int index = -1;

                if (!(txtNome.getText().equals("")) && !(txtNome.getText().equals(" ")))
                    index = Integer.parseInt(JHiddenLabel.getText());
                salvar(table, dono, index);
                txtNome.setText("");
                txtAdocao.setText("");
                txtEndereco.setText("");
            }
        });
        
        btnExcluir.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e)
            {
                JTable table = tbTabela;
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int[] rows = table.getSelectedRows();
                for(int i = 0; i < rows.length ; i++) {
                  model.removeRow(rows[i]-i);
                }
            }
        });

        javax.swing.GroupLayout jPanelDonoLayout = new javax.swing.GroupLayout(jPanelDono);
        jPanelDono.setLayout(jPanelDonoLayout);
        jPanelDonoLayout.setHorizontalGroup(
            jPanelDonoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDonoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDonoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblEndereco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAdocao, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNome, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDonoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNome)
                    .addComponent(txtAdocao, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtEndereco, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanelDonoLayout.setVerticalGroup(
            jPanelDonoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDonoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDonoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNome)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDonoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAdocao)
                    .addComponent(txtAdocao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDonoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEndereco)
                    .addComponent(txtEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanelDono, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnListar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnNovo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExcluir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConfirmar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelDono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnListar)
                            .addComponent(btnNovo)
                            .addComponent(btnExcluir)
                            .addComponent(btnConfirmar))))
                .addContainerGap(119, Short.MAX_VALUE))
        );
        
        pack();
    }
    
    public void salvar(JTable tbTabela, Dono dono, int index) {
        try {
            JTable table = tbTabela;
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            TreeMap<Animal, Integer> map = new TreeMap<Animal, Integer>(new AnimalComp());
            
            if (index != -1 ) {
                tbTabela.getModel().setValueAt(txtNome.getText(), index, 6);
                tbTabela.getModel().setValueAt(txtEndereco.getText(), index, 8);
                tbTabela.getModel().setValueAt(txtAdocao.getText(), index, 7);
            }

            for (int count = 0; count < model.getRowCount(); count++) {
                String nome = model.getValueAt(count, 0).toString().toUpperCase();
                String tipo = model.getValueAt(count, 1).toString().toUpperCase();
                LocalDate chegada = LocalDate.parse(model.getValueAt(count, 2).toString(), dtf);
                int idade = Integer.parseInt(model.getValueAt(count, 3).toString());
                double peso = Double.parseDouble(model.getValueAt(count, 4).toString());
                String nomeDono = " ";
                String endereco = " ";
                LocalDate adocao = LocalDate.MIN;
                
                if (model.getValueAt(count, 6) != null) {
                    if (!(model.getValueAt(count, 6).toString().equals("")) && !(model.getValueAt(count, 6).toString().equals(" "))) {
                        nomeDono = model.getValueAt(count, 6).toString().toUpperCase();
                        endereco = model.getValueAt(count, 8).toString().toUpperCase();
                        adocao = LocalDate.parse(model.getValueAt(count, 7).toString(), dtf);
                    }
                }

                Animal animal = new Animal(tipo, idade, nome, chegada, peso, nomeDono, endereco, adocao);

                if (map.get(animal) == null) {
                    map.put(animal, 1);
                }
                else {
                    int quant = map.get(animal).intValue();
                    quant++;
                    map.put(animal, quant);
                }
            }   

            if (inserirDados(map)) {
                JFrame frame = new JFrame("JOptionPane showMessageDialog example");
                JOptionPane.showMessageDialog(frame, "Salvo com sucesso!");

                atualizaTabela(tbTabela);
            }
        }
        catch (Exception e) {
            JFrame frame = new JFrame("JOptionPane showMessageDialog example");
            JOptionPane.showMessageDialog(frame, "Erro!");
        }                
    }
        
    private void atualizaTabela(JTable tbTabela) {
        TreeMap<Animal, Integer> map = new TreeMap<Animal, Integer>(new AnimalComp());
        map = lerDados(map);
        
        Set<Animal> keys = map.keySet();
        ArrayList<Animal> animais = new ArrayList<Animal>();
       
        for (Animal animal: keys) {
            animais.add(animal);
        }        
        
        for (int i = 0; i < animais.size(); i++) {
            for (int j = 0; j < animais.get(i).retornaArray().length; j++)
                tbTabela.getModel().setValueAt(animais.get(i).retornaArray()[j], i, j);
        }
        
        Action openAdocao = new AbstractAction()
        {
            public void actionPerformed(ActionEvent e)
            {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                int modelRow = Integer.valueOf( e.getActionCommand() );
                Dono dono = new Dono(animais.get(modelRow).getNomeDono(), animais.get(modelRow).getAdocao(), animais.get(modelRow).getEndereco());
                String dataFormatada = dono.getAdocao().format(dtf);
                
                txtNome.setText(dono.getNome());
                if (!dono.getAdocao().equals(LocalDate.MIN))
                    txtAdocao.setText(dataFormatada);
                else
                    txtAdocao.setText("");
                txtEndereco.setText(dono.getEndereco());
                
                jPanelDono.setVisible(true);
                JHiddenLabel.setText(String.valueOf(modelRow));
            }
        };

        ButtonColumn buttonColumn = new ButtonColumn(tbTabela, openAdocao, 5);
        buttonColumn.setMnemonic(KeyEvent.VK_D);
    }
    
    public TreeMap<Animal, Integer> lerDados(TreeMap<Animal, Integer> map) {
        InputStream arquivo = this.getClass().getClassLoader().getResourceAsStream("data/mapObject.data");
        
        try {
            ObjectInputStream obj_in = new ObjectInputStream(arquivo);
            
            map = (TreeMap<Animal, Integer>) obj_in.readObject();
            
            obj_in.close();
        }
        catch( Exception exc ) {
            System.out.println( "Erro ao trazer dados." );
        }
        
        return map;
    }
    
    public boolean inserirDados(TreeMap<Animal, Integer> map) {
        try {
            OutputStream arquivo = new FileOutputStream("data/mapObject.data");
        
            ObjectOutputStream obj_out = new ObjectOutputStream(arquivo);
            obj_out.writeObject(map);
            
            obj_out.close();
        }
        catch( Exception exc ) {
            System.out.println( "Erro ao inserir dados." );
            return false;
        }
        
        return true;
    }
}