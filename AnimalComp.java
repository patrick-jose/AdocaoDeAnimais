import java.util.Comparator;
import java.io.Serializable;

public class AnimalComp implements Comparator<Animal>, Serializable
{
    @Override
    public int compare(Animal a1, Animal a2) {
        boolean nome = a1.getNome().equalsIgnoreCase(a2.getNome());
        boolean tipo = a1.getTipo().equalsIgnoreCase(a2.getTipo());
        boolean idade = a1.getIdade() == a2.getIdade();
        boolean chegada = a1.getChegada().isEqual(a2.getChegada());
        boolean peso = a1.getPeso() == a2.getPeso();
    
        if (nome && tipo && idade && chegada && peso)
            return 0;
        else 
            return 1;
    }
}
