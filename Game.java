import javax.sql.rowset.spi.SyncResolver;
import javax.swing.plaf.synth.SynthOptionPaneUI;


/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Inventario inventario;

        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        inventario = new Inventario();

    }


    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room enfermaria, diretoria, biblioteca, laboratorio, refeitorio, portao, sala, quadra, piscina;
        Item agenda, livro, bequer, fechadura, caneta, bola, agua, vazio, chave;

        //Cria os itens

       agenda = new Item("agenda","Possui algumas anotações, porém nada de importante.");
       livro = new Item("livro","Um livro de ciências, nada especial.");
       bequer = new Item("bequer","Pode ser usado para carregar algum líquido.");
       fechadura = new Item("fechadura", "Parece precisar de algum tipo de chave.");
       caneta = new Item("caneta","Pode ser usada para rabiscar.");
       bola = new Item("bola","Uma bola de futebol, pode ser usada para atingir um objeto longíquo.");
       agua = new Item("agua","Água não potável.");
       vazio = new Item("","");
       chave = new Item("chave","Uma chave pequena, parece ser usada para abrir alguma fechadura.");
       
        // Cria as salas
        
        enfermaria = new Room("na enfermaria",vazio);
        diretoria = new Room("na diretoria",agenda);
        biblioteca = new Room("na biblioteca",livro);
        laboratorio = new Room("no laboratorio",bequer);
        refeitorio = new Room("no refeitorio",chave);
        portao = new Room("no portao",fechadura);
        sala = new Room("em uma sala de aula",caneta);
        quadra = new Room("na quadra",bola);
        piscina = new Room("na piscina",agua);
        
        // initialise room exits
        enfermaria.setExit("norte", laboratorio);
        enfermaria.setExit("sul", refeitorio);
        enfermaria.setExit("leste", sala);
        enfermaria.setExit("oeste", biblioteca);

        diretoria.setExit("leste", biblioteca);

        biblioteca.setExit("leste", enfermaria);
        biblioteca.setExit("oeste", diretoria);

        laboratorio.setExit("sul", enfermaria);

        refeitorio.setExit("norte", enfermaria);
        refeitorio.setExit("sul", portao);

        portao.setExit("norte", refeitorio);

        sala.setExit("leste", quadra);
        sala.setExit("oeste", enfermaria);

        quadra.setExit("oeste", sala);
        quadra.setExit("sul", piscina);

        piscina.setExit("norte", quadra);

        currentRoom = enfermaria;  // Começa o jogo na enfermaria
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Obrigado por jogar. Adeus.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Bem vindo ao jogo Escola Abandonada!");
        System.out.println("Escola Abandonada é um jogo de investigação e exploração.");
        System.out.println("Digite '" + CommandWord.HELP + "' se você precisa de ajuda.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("Sou sensível a contexto");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case PROCURAR:
                procurarEm(command);
                break;

            case PEGAR:
                pegarItem(command);
                break;

            case MOSTRAR_INVENTARIO:
                inventario.mostrarInventario();
                break;

            case ACAO:
                wantToQuit = realizarAcao(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("Você esta perdido em uma escola abandonada");
        System.out.println("Você está na enfermaria.");
        System.out.println();
        System.out.println("Seus comandos são:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Ir aonde?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("Não há saída!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    private void procurarEm(Command command) {
        
        System.out.println(currentRoom.procurar());
    }

    private void pegarItem(Command command) {
        Item item_clonado;

        item_clonado = new Item("","");

        if (currentRoom.getItem().getObjeto() == "" ) System.out.println("Não há nada para pegar");
        else if (currentRoom.getItem().getObjeto() == "fechadura"){
            System.out.println("Não há como pegar a fechadura");
        }
        else {
            item_clonado = currentRoom.getItem().clonar_Item(currentRoom.getItem(), item_clonado);
            inventario.addItem(item_clonado);
            System.out.println("O item " + currentRoom.getItem().getObjeto() + " foi adicionado");
            currentRoom.getItem().excluir_item();

        }
    }

    private Boolean realizarAcao(Command command){
        Boolean concluiu = false;
         
        if (currentRoom.getItem().getObjeto() == "fechadura") {
                if (inventario.procurarItem("chave")) {
                    System.out.println("Você concluiu o jogo!");
                    concluiu = true;
                  }  else {
                System.out.println("Você não possui a chave.");
            }
        } else System.out.println("Não há ação para ser realizada nesse lugar.");
     return concluiu;
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Sair do que?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}