import java.util.HashMap;

public class Item {

    private String objeto;
    private String descricao;
    

    public Item(String objeto, String descricao) {
        this.objeto = objeto;
        this.descricao = descricao;
    }

    public String getObjeto(){
        return objeto;
    }

    public String getDescricao(){
        return descricao;
    }

    public void excluir_item(){
        objeto = "";
        descricao = "";
    }

    public Item clonar_Item(Item item, Item item2){
       return item2 = new Item(item.getObjeto(),item.getDescricao());
    }

    public String detalhesItem(){
        return "\nItem: " + objeto + "\n" + "Descrição: " + descricao;
    }
    
}