
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Animal implements Serializable
{
    private String tipo;
    private int idade;
    private String nome;
    private LocalDate chegada;
    private double peso;
    private Dono dono;
    
    public Animal(String t, int i, String n, LocalDate c, double p) {
        tipo = t;
        idade = i;
        nome = n;
        chegada = c;
        peso = p;
        
        dono = new Dono(" ", LocalDate.MIN, " ");
    }
    
    public Animal(String t, int i, String n, LocalDate c, double p, String nd, String e, LocalDate a) {
        tipo = t;
        idade = i;
        nome = n;
        chegada = c;
        peso = p;
        
        dono = new Dono(nd, a, e);
    }
    
    public String getTipo() {
        return this.tipo;
    }
    
    public int getIdade() {
        return this.idade;
    }
    
    public String getNome() {
        return this.nome;
    }
    
    public LocalDate getChegada() {
        return this.chegada;
    }
    
    public double getPeso() {
        return this.peso;
    }
    
    public String getNomeDono() {
        return this.dono.getNome();
    }
    
    public String getEndereco() {
        return this.dono.getEndereco();
    }
    
    public LocalDate getAdocao() {
        return this.dono.getAdocao();
    }
    
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = this.chegada.format(dtf);
        
        return "\n\nNome: " + this.nome + 
               "\nTipo: " + this.tipo + 
               "\nChegada: " + dataFormatada + 
               "\nIdade:" + this.idade + " anos" +
               "\nPeso:" + this.peso + "kg";
    }
    
    public Object[] retornaArray() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String chegadaFormatada = this.chegada.format(dtf);
        String adocaoFormatada = this.dono.getAdocao().format(dtf);
        
        Object[] array = { 
                            this.nome,
                            this.tipo,
                            chegadaFormatada,
                            this.idade,
                            this.peso, 
                            "Adoção", 
                            this.dono.getNome(), 
                            adocaoFormatada, 
                            this.dono.getEndereco()
                         };
        
        return array;
    }
    
    public Object[] retornaTabela(Animal animal) {
        Object[] retorno = { 
                                animal.getNome(), 
                                animal.getTipo(), 
                                animal.getIdade(), 
                                animal.getChegada(),
                                animal.getPeso()
                           };
        return retorno;
    }
}
