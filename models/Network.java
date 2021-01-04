package models;


import Algo.DijkstrasAlgorithm;
import com.sun.imageio.spi.RAFImageOutputStreamSpi;
import org.omg.CORBA.INTERNAL;

import java.util.*;

public class Network {
    private List<Router> listRouter;
    private int[][] dijkstraMap;
    private Map<String, Integer> mapNameToId;
    private List<List<Router>> listLinkTruncate;

    public Map<Router, List<Router>> getListLink() {
        return listLink;
    }

    public void setListLink(Map<Router, List<Router>> listLink) {
        this.listLink = listLink;
    }

    private Map<Router, List<Router>> listLink;
    public List<Router> getListRouter() {
        return listRouter;
    }

    public void setListRouter(List<Router> listRouter) {
        this.listRouter = listRouter;
    }

    public Network() {
        this.listRouter = new ArrayList<Router>();
        this.mapNameToId = new HashMap<String, Integer>();
        this.listLink = new HashMap<>();
        this.listLinkTruncate = new ArrayList<>();
    }
    public void addRouter(Router router) {
        this.listRouter.add(router);
        this.mapNameToId.put(router.getName(), router.getId());
    }
    public int[][] generateDijkstraMap(){
        int n = this.listRouter.size();

        for(int i = 0; i < n; i++) {
            for(int j = i + 1; j < n; j++){
                if(!this.listRouter.get(i).isSameMessages(this.listRouter.get(j))) {
                    System.out.println("All routers have not same router table!...");
                    return null;
                }
            }
        }
        this.dijkstraMap = new int[n][n];
        for(int i = 0; i < n; i++) {
            for(int j =0; j < n; j++) {
                if (i!= j) {
                    this.dijkstraMap[i][j] = -1;
                }
            }
        }
        Router router = this.listRouter.get(0);
        for(LMessage message: router.getListLMessage()) {
            int sourceId = this.mapNameToId.get( message.getSourceName());
            for(LinkCount count: message.getListLink()) {
                int desId = this.mapNameToId.get(count.getDestinationName());
                this.dijkstraMap[sourceId][desId] = count.getCost();
            }
        }
        return this.dijkstraMap;
    }

    public int[][] generateDijsktraTemp() {
        int n = this.listRouter.size();
        int[][] result = new int[n][n];
        List<LMessage> listTemp = new ArrayList<>();
        for(Router r: this.getListRouter()) {
            listTemp.add(r.generateLMessage());
            r.resetLMessage();
        }
        for(LMessage message: listTemp) {
            List<String> listDestinationName = new ArrayList<>();
            int sourceId = this.mapNameToId.get( message.getSourceName());
            for(LinkCount count: message.getListLink()) {
                int desId = this.mapNameToId.get(count.getDestinationName());
                result[sourceId][desId] = count.getCost();
                listDestinationName.add(count.getDestinationName());
            }

            for(Router r: this.getListRouter()) {
                if(r.getName() != message.getSourceName() && !listDestinationName.contains(r.getName())) {
                    result[sourceId][r.getId()] = -1;
                }
            }
        }

        return result;

    }


    public String getDijkstraMapStr() {
        int[][] result = this.generateDijkstraMap();
        String str = "";
        for(int i = 0; i < result.length; i++) {
            for(int j = 0; j < result[i].length; j++) {
                System.out.print(result[i][j]+"\t");
                str += result[i][j]+"\t";
            }
            System.out.println();
            str += "\n";
        }
        return str;
    }

    public void distributeLsaMessages() throws InterruptedException {
        for(Router root: this.getListRouter()) {
//            System.out.println("=================================================");
//            System.out.println("Start flooding in router "+root.getName()+".........");
            LMessage message = root.generateLMessage();
            Stack<Router> stack = new Stack<Router>();
            Map<Router, Boolean> status = new HashMap<Router, Boolean>();
            for(Router r: this.getListRouter()) {
                status.put(r, false);
            }
            status.put(root, true);
            stack.push(root);
            while(!checkAllReceived(status)) {
                Router temp = stack.pop();
                for(Router child: temp.getListNeigh().keySet()) {
                    if(status.get(child) == false) {
                        temp.sendLMessage(child, message);
                        status.put(child, true);
                        if(isCompleted(status, child)) {
                           continue;
                        }
                        stack.push(child);
                    }
                }
            }

        }
    }

    private boolean isCompleted(Map<Router, Boolean> status, Router root) {
        for(Router child: root.getListNeigh().keySet()) {
            if(status.get(child) == false) {
                return false;
            }
        }
        return true;
    }

    public boolean checkAllReceived(Map<Router, Boolean> status) {
        for(Router r: status.keySet()) {
            if(status.get(r) == false) {
                return false;
            }
        }
        return true;
    }

    public boolean checkRouterHasFullLsa() {
        for(Router router: this.getListRouter()) {
            for(Router temp: this.getListRouter()) {
                boolean checkExist = false;
                for(LMessage message: router.getListLMessage()) {
                    if(message.getSourceName() == temp.getName()) {
                      checkExist = true;
                      break;
                    }
                }

                if(!checkExist) {
                    return false;
                }
            }
        }
        return true;
    }


    public List<Router> distributeLsaMessageFrom(Router root) throws InterruptedException {
        root.sendLMessage(root, root.generateLMessage());
        List<Router> trackingList = new ArrayList<>();
        trackingList.add(root);
        System.out.println("=================================================");
        System.out.println("Start flooding in router "+root.getName()+" .........");
        LMessage message = root.generateLMessage();
        Stack<Router> stack = new Stack<Router>();
        Map<Router, Boolean> status = new HashMap<Router, Boolean>();
        for(Router r: this.getListRouter()) {
            status.put(r, false);
        }
        status.put(root, true);
        stack.push(root);
        while(!checkAllReceived(status)) {
            Router temp = stack.pop();
            for(Router child: temp.getListNeigh().keySet()) {
                if(status.get(child) == false) {
                    temp.sendLMessage(child, message);
                    trackingList.add(child);
                    status.put(child, true);
                    if(isCompleted(status, child)) {
                        continue;
                    }
                    stack.push(child);
                }
            }
        }

        return trackingList;
    }
    public List<Router> distributeLsaMessageFrom(Router root, LMessage lMessage) throws InterruptedException {
        root.sendLMessage(root, lMessage);
        List<Router> trackingList = new ArrayList<>();
        trackingList.add(root);
        System.out.println("=================================================");
        System.out.println("Start flooding in router "+root.getName()+" .........");
        LMessage message = lMessage;
        Stack<Router> stack = new Stack<Router>();
        Map<Router, Boolean> status = new HashMap<Router, Boolean>();
        for(Router r: this.getListRouter()) {
            status.put(r, false);
        }
        status.put(root, true);
        stack.push(root);
        while(!checkAllReceived(status)) {
            Router temp = stack.pop();
            for(Router child: temp.getListNeigh().keySet()) {
                if(status.get(child) == false) {
                    temp.sendLMessage(child, message);
                    trackingList.add(child);
                    status.put(child, true);
                    if(isCompleted(status, child)) {
                        continue;
                    }
                    stack.push(child);
                }
            }
        }

        return trackingList;
    }

    public List<List<Router>> getListLinkTruncate() {
        return listLinkTruncate;
    }

    public void setListLinkTruncate(List<List<Router>> listLinkTruncate) {
        this.listLinkTruncate = listLinkTruncate;
    }

    public void addNewLink(Router routerA, Router routerB) {
        if(routerA.getId() < routerB.getId()) {
        this.listLinkTruncate.add(Arrays.asList(routerA, routerB));

        }else{
        this.listLinkTruncate.add(Arrays.asList(routerB, routerA));

        }
        if(!this.getListLink().containsKey(routerA)) {
           List<Router> tempList = new ArrayList<>();
           tempList.add(routerB);
           this.getListLink().put(routerA, tempList);
        }else {
            this.getListLink().get(routerA).add(routerB);
        }
        if(!this.getListLink().containsKey(routerB)) {
            List<Router> tempList = new ArrayList<>();
            tempList.add(routerA);
            this.getListLink().put(routerB, tempList);
        }else {
            this.getListLink().get(routerB).add(routerA);
        }
        routerA.addNewRouter(routerB);
        routerB.addNewRouter(routerA);
    }
    public Router getRouterFromName(String name) {
        for(Router router: this.getListRouter()) {
            if(router.getName() == name) {
                return router;
            }
        }
        return null;
    }

    public void showListRouter() {
        for(Router router: this.getListRouter()) {
            System.out.println(router);
        }
    }

    public boolean isExistLink(Router r1, Router r2) {
        if(this.listLink.containsKey(r1)) {
            for(Router temp: this.listLink.get(r1)) {
                if(temp.equals(r2)) {
                    return true;
                }
            }
        }
        if(this.listLink.containsKey(r2)) {
            for(Router temp: this.listLink.get(r2)) {
                for(Router router: this.listLink.get(r2)) {
                    if(router.equals(r1)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Map<Integer, String> routeTableInRouter(Router router) {
        Map<Integer, String> result = new HashMap<>();
        int[][] dijMap = this.generateDijkstraMap();
        DijkstrasAlgorithm algo = new DijkstrasAlgorithm();
        algo.dijkstra(dijMap, router.getId());
        int[] parents = algo.getParents();
        int[] shortestDistance = algo.getShortestDistance();

        for(int i = 0; i  < shortestDistance.length; i++) {
            String str = "";
            if(i == router.getId() ) {
                result.put(i, 0+"\t"+i);
            }else {
                str += "";
                str += i +"\t";
                int curr = i;
                while (curr!= -1) {
                    curr = parents[curr];
                    str += (curr != -1) ? curr +"\t":"";
                }
                List<String> temp  = Arrays.asList(str.split("\t"));
                String tempStr = "";
                for(int j = temp.size() - 1; j >= 0; j--) {
                    tempStr += temp.get(j) +"\t";
                }

                result.put(i,shortestDistance[i]+"\t"+tempStr);
            }

        }
        return result;
    }

    public Router getRouterById(int id) {
        for(Router router: this.getListRouter()) {
            if(id == router.getId()) {
                return router;
            }
        }
        return null;
    }

    public Map<String, String> routingTableMap(Router router) {
        Map<Integer, String> routeTable = routeTableInRouter(router) ;
        Map<String, String> result = new HashMap<>();
        for(Integer id: routeTable.keySet()) {
            Router temp = getRouterById(id);
            String cost = routeTable.get(id).split("\t")[0];
            String nextHop = "";
            if(routeTable.get(id).split("\t").length < 3) {
                nextHop = "~";
                result.put(temp.getName(), cost +"\t" + nextHop);
            }else {
                nextHop = routeTable.get(id).split("\t")[2];
                result.put(temp.getName(), cost+"\t"+getRouterById(Integer.valueOf(nextHop)).getName());
            }
        }
        return result;
    }

    public void increaseAge(Router router) {
        for(int i = 0; i < router.getListLMessage().size(); i++) {
            LMessage message = router.getListLMessage().get(i);
            message.setAge(message.getAge() + 1);
            router.getListLMessage().set(i, message);
        }
    }

    public  int getMaxAgeCurr() {
        if(this.getListRouter().get(0).getListLMessage().isEmpty()) {
            return 0;
        }
        return this.getListRouter().get(0).getListLMessage().get(0).getAge();
    }



}
