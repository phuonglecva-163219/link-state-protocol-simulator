package models;

import com.sun.org.apache.bcel.internal.generic.FieldOrMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Router {
    private String name;
    private int id;
    private Map<Router, Integer> listNeigh;
    private List<LMessage> listLMessage;
    private Location location;
    public Router(String name, int id, Location location){
        this.name = name;
        this.id = id;
        this.listNeigh = new HashMap<Router, Integer>();
        this.listLMessage = new ArrayList<LMessage>();
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<LMessage> getListLMessage() {
        return listLMessage;
    }

    public void setListLMessage(List<LMessage> listLMessage) {
        this.listLMessage = listLMessage;
    }

    public Router(String name, Map<Router, Integer> listNeigh) {
        this.name = name;
        this.listNeigh = listNeigh;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Router, Integer> getListNeigh() {
        return listNeigh;
    }

    public void setListNeigh(Map<Router, Integer> listNeigh) {
        this.listNeigh = listNeigh;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void addNewRouter(Router router) {
        int new_cost =(int) router.getLocation().distanceTo(this.getLocation());

        this.listNeigh.put(router, new_cost);

    }

    public LMessage generateLMessage() {
        List<LinkCount> l = new ArrayList<LinkCount>();
        for(Router router: this.listNeigh.keySet()) {
            l.add(new LinkCount(router.getName(), this.listNeigh.get(router)));
        }
//        int age = 5 + (int)(Math.random() * ((20 - 5) + 1));
        LMessage message = new LMessage(this.name, 1, 0, l);
        return message;
    }
    public void addLmessage(LMessage lMessage) {
        if(this.listLMessage.isEmpty()) {
            this.listLMessage.add(this.generateLMessage());
        }
//        for(LMessage mess: this.getListLMessage()) {
//            if(mess.getSourceName() == lMessage.getSourceName()) {
//                if(mess.getSeq() <= lMessage.getSeq()) {
//                    mess = lMessage;
//                    System.out.println(true);
//                    return;
//                }
//            }
//        }
        int pos = -1;
        for(int i = 0; i < this.getListLMessage().size(); i++) {
            LMessage mess = this.getListLMessage().get(i);
            if(mess.getSourceName() == lMessage.getSourceName()) {
                if(mess.getSeq() <= lMessage.getSeq()) {
                    pos = i;
                }else {
                    return;
                }
                break;
            }
        }
        if(pos == -1 ) {
            this.listLMessage.add(lMessage);
        }else {
            this.getListLMessage().set(pos, lMessage);
        }
//        for(LMessage mess: this.getListLMessage()) {
//            if(mess.getSourceName() == lMessage.getSourceName()) {
//                if(mess.getSeq() <= lMessage.getSeq()) {
//                    this.listLMessage.add(lMessage);
//                    return;
//                }
//            }
//        }
//        this.listLMessage.add(lMessage);


    }

    public LMessage getMessageHasSameSource(LMessage message) {
        for(LMessage lMessage: this.getListLMessage()) {
            if(lMessage.getSourceName() == message.getSourceName()) {
                return lMessage;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String listNeighStr = "[";
        for(Router r: this.getListNeigh().keySet()) {
            String str = "{name: " + r.getName() + ", cost: " + this.getListNeigh().get(r) +"}, ";
            listNeighStr += str;
        }
        listNeighStr += "]";
        return "Router{" +
                "name='" + name + '\'' +
                "location='" + location.getName() + '\'' +
                ", id=" + id +
                ", listNeigh=" + listNeighStr +
                ", listLMessage=" + listLMessage +
                '}';
    }

    public void sendLMessage(Router router) throws InterruptedException {
        if(this.getId() == router.getId()) {
            return;
        }
        System.out.println("Sending LSA message from "+ this.getName() +" to  router " + router.name);
        System.out.println(".");
//        Thread.sleep(1000);
        System.out.println(".");
        System.out.println("completed");
        router.addLmessage(this.generateLMessage());
    }

    public void showListMessage() {
        System.out.println("---------------------------");
        System.out.println("This is LSA Message in router " + this.getName() + ": ");
        for(LMessage lMessage: this.listLMessage) {
            System.out.println(lMessage);
        }
        System.out.println("----------------------------");
    }

    public boolean isSameMessages(Router router) {
        if (this.listLMessage.size() == router.getListLMessage().size()) {
            return true;
        }
        return false;
    }

    public void sendLMessage(Router destination, LMessage lMessage) throws InterruptedException {
//        if(this.getId() == destination.getId() || lMessage.getSourceName() == destination.getName()) {
//            return;
//        }
//        System.out.println("Sending LSA message of Router "+ lMessage.getSourceName()+" from "+ this.getName() +" to  router " + destination.name);
//        System.out.println(".");
//        Thread.sleep(200);
//        System.out.println(".");
//        System.out.println("completed");
        destination.addLmessage(lMessage);
    }

    public void requestLsaMessageFrom(Router root, Network network) throws InterruptedException {
        network.distributeLsaMessageFrom(root);
    }



    public void resetLMessage() {
        this.listLMessage = new ArrayList<>();
    }
}
