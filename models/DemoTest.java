package models;

import Algo.DirectedConnectivityDfs;

import java.util.*;

public class DemoTest {

    public static Network generateNework(int noEdges) {
        List<Location> listLocation = Location.getListLocation();
        Network net = new Network();
        for(int i = 65; i < noEdges + 65; i++) {
            Router tempRouter = new Router(String.valueOf((char) i ), i - 65, listLocation.get((i - 65)));
            net.addRouter(
                    tempRouter
            );
        }
        int[][] map = null;
        do{

            for(Router router: net.getListRouter()) {
                Random rand = new Random();
                rand.setSeed(123566466);
                int n = new Random().nextInt(4);
                while(n > 0) {
                    Router router1 = net.getListRouter().get(rand.nextInt(net.getListRouter().size()));
                    net.addNewLink(router, router1);
                    n--;
                }

            }
            map = net.generateDijsktraTemp();
//            for(int i = 0; i < map.length; i++) {
//                for(int j = 0; j < map[i].length; j++) {
//                    System.out.print(map[i][j] + "\t");
//                }
//                System.out.println();
//            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }while (!new DirectedConnectivityDfs().isConnected(net));
        return  net;

    }

    public static void main(String[] args) throws InterruptedException {
        List<Integer> listNumEdge = new ArrayList<>();

        for(int i = 10; i < 300; i = i + 10) {
            listNumEdge.add(i);
        }
        Map<Integer, Float> map = new HashMap<>();
        Map<Integer, Integer> linkMap = new HashMap<>();
        for(Integer i: listNumEdge) {

            Network netDemo = DemoTest.generateNework(i);
            long start = System.currentTimeMillis();
            netDemo.distributeLsaMessages();
            long elapsedTimeMillis = System.currentTimeMillis()-start;
            float elapsedTimeSec = elapsedTimeMillis;
            map.put(i, elapsedTimeSec);
            System.out.println("======================================");
            System.out.println("Number of node: "+ netDemo.getListRouter().size());
            System.out.println("Number of link: "+ new HashSet<>(netDemo.getListLinkTruncate()).size());
            System.out.println("Time to distributed message: "+elapsedTimeSec + "ms.");
            System.out.println("======================================");
            linkMap.put(i, new HashSet<>(netDemo.getListLinkTruncate()).size());

        }
        SortedSet<Integer> keys = new TreeSet<>(map.keySet());
        for(Integer key: keys) {
            System.out.println(key +"\t" + map.get(key));
        }
        System.out.println("=============================");
        for(Integer key: keys) {
            System.out.println(key +"\t" + linkMap.get(key));
        }


    }
}
