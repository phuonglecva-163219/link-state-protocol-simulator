import models.*;
import Algo.*;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DemoMain {

    public static Network getNetWork() {
        List<Location> listLocation = Location.getListLocation();
        Router r1 = new Router("A", 0, listLocation.get(0));
        Router r2 = new Router("B", 1, listLocation.get(1));
        Router r3 = new Router("C", 2, listLocation.get(2));
        Router r4 = new Router("D", 3, listLocation.get(3));
        Router r5 = new Router("E", 4, listLocation.get(4));
        Network network = new Network();
        network.addRouter(r1);
        network.addRouter(r2);
        network.addRouter(r3);
        network.addRouter(r4);
        network.addRouter(r5);

        network.addNewLink(r1, r2);
        network.addNewLink(r1, r3);
        network.addNewLink(r1, r4);
        network.addNewLink(r2, r4);
        network.addNewLink(r3, r5);
        network.addNewLink(r4, r5);
//        network.addNewLink(r5, r1);
        System.out.println(r5);
        return network;
    }
    public static void main(String[] args) throws InterruptedException {
        List<Location> listLocation = Location.getListLocation();
        Router r1 = new Router("A", 0, listLocation.get(0));
        Router r2 = new Router("B", 1, listLocation.get(1));
        Router r3 = new Router("C", 2, listLocation.get(2));
        Router r4 = new Router("D", 3, listLocation.get(3));
        Router r5 = new Router("E", 4, listLocation.get(4));
        Network network = new Network();
        network.addRouter(r1);
        network.addRouter(r2);
        network.addRouter(r3);
        network.addRouter(r4);
        network.addRouter(r5);

//        Show list of router in network
        System.out.println("++++++++++++++++++++++++++++++++++++++");
        network.showListRouter();
        System.out.println("++++++++++++++++++++++++++++++++++++++");

        network.addNewLink(r1, r2);
        network.addNewLink(r1, r3);
        network.addNewLink(r1, r4);
        network.addNewLink(r2, r4);
        network.addNewLink(r3, r5);
        network.addNewLink(r4, r5);

        System.out.println("================================");
        System.out.println(r1.generateLMessage());
        System.out.println(r2.generateLMessage());
        System.out.println(r3.generateLMessage());
        System.out.println(r4.generateLMessage());
        System.out.println(r5.generateLMessage());
        System.out.println("================================");

        network.showListRouter();
        network.distributeLsaMessages();
//            network.distributeLsaMessageFrom(r1);
//            network.distributeLsaMessageFrom(r2);
//            network.distributeLsaMessageFrom(r3);
//            network.distributeLsaMessageFrom(r4);
//            network.distributeLsaMessageFrom(r5);
//        System.out.println(network.checkRouterHasFullLsa());
        //        r1.sendLMessage(r5, r2.generateLMessage());
//        for(Router r: network.getListRouter()) {
//            r.showListMessage();
//        }
        int[][] result = network.generateDijkstraMap();
        for(int i = 0; i < result.length; i++) {
            for(int j = 0; j < result[i].length; j++) {
                System.out.print(result[i][j]+"\t");
            }
            System.out.println();
        }
        DijkstrasAlgorithm algorithm = new DijkstrasAlgorithm();
        System.out.println("========================");
        algorithm.dijkstra(result, 0);
        System.out.println();

        int[] parents = algorithm.getParents();
        for(Integer i: parents) {
            System.out.print(i +"\t");
        }
         System.out.println("========================");

        for(Router r: network.distributeLsaMessageFrom(r2)) {
            System.out.println(r.getName());
        }

        Map<Integer, String> map = network.routeTableInRouter(network.getListRouter().get(0));
        System.out.println();
        for(Integer key: map.keySet()) {
            System.out.println(key +"\t" + map.get(key));
        }
        System.out.println(map.size());
        System.out.println("=================================");
        Map<String, String> map1 = network.routingTableMap(r1);
        System.out.println();
        for(String s: map1.keySet()) {
            System.out.println(s +"\t" + map1.get(s));
        }
        network.increaseAge(r1);
        System.out.println(network.getListRouter().get(0));


    }
}
