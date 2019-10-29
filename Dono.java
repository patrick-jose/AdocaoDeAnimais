
import java.time.LocalDate;
import java.io.Serializable;

public class Dono implements Serializable
{
    private String nome;
    private LocalDate adocao;
    private String endereco;
    
    public Dono(String n, LocalDate d, String e) {
        nome = n;
        adocao = d;
        endereco = e;
    }
    
    public Dono() {
    }

    public String getNome() {
        return this.nome;
    }
    
    public String getEndereco() {
        return this.endereco;
    }
    
    public LocalDate getAdocao() {
        return this.adocao;
    }
}
