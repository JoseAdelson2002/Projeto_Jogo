import java.util.ArrayList;

public class Inventario {
    private ArrayList<Item> inventario;

    public Inventario(){
        inventario = new ArrayList<>();
    }

    public void addItem(Item item){
            inventario.add(item);
        }

    public Boolean procurarItem(String item){
        Boolean existe = false;

        for (int i = 0; i < inventario.size(); i++) {
           if (inventario.get(i).getObjeto() == item) existe = true;
            
        }
        return existe;
    }

    public void retirarItem(Item item){
            inventario.remove(item);
        }

    public void mostrarInventario(){
            for (int i = 0; i < inventario.size(); i++){
                System.out.println(inventario.get(i).detalhesItem());
            }
        }
}
