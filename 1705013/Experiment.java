import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Experiment {
    public static ArrayList<Board> children=new ArrayList<>(100);
    public static void main(String[] args) throws IOException {
        FileWriter fileWriter = new FileWriter("E:\\result.txt");
        int[] player1=new int[10];
        int[] player2=new int[10];
        for(int i=0;i<7;i++){
            if(i==0){
                player1[i]=0;
                player2[i]=0;
            }
            else{
                player1[i]=4;
                player2[i]=4;
            }
        }
        printBoard(player1, player2);
        int  count=3,player1Win=0,player2Win=0,drawn=0;
        while(count<=3){
            int depth=1;
            while(depth<=15){
                Board board=new Board(player1,player2,false);
                int turn=1;
                while (true){
                    double val=alphaBeta(board,turn,-100,100,depth,count,turn);
                    for(int i=0;i<children.size();i++){
                        if(val==children.get(i).getHval()){
                            board=children.get(i);
                            break;
                        }
                    }
                    if(board.getCapture()){
                        //System.out.println("Good Capture!!!");
                    }
                    if(!board.getFree()){
                        turn=3-turn;
                    }
                    else{
                        //System.out.println("Free turn");
                    }
                    if(EndGame(board)){
                        for(int i=1;i<7;i++){
                            board.getPlayer1()[0]+=board.getPlayer1()[i];
                            board.getPlayer2()[0]+=board.getPlayer2()[i];
                            board.getPlayer1()[i]=0;
                            board.getPlayer2()[i]=0;
                        }
                        //printBoard(board.getPlayer1(),board.getPlayer2());
                        System.out.println("Using Huristic: "+count+" And at depth: "+depth);
                        fileWriter.append("Using Huristic: "+count+" And at depth: "+depth+"\n");
                        //System.out.println("Game Over!!!");
                        if(board.getPlayer1()[0]>board.getPlayer2()[0]){
                            player1Win++;
                            System.out.println("Player1 Wins");
                            fileWriter.append("Player1 Wins\n");
                        }
                        else if(board.getPlayer2()[0]>board.getPlayer1()[0]){
                            player2Win++;
                            System.out.println("Player2 wins");
                            fileWriter.append("Player2 wins\n");
                        }
                        else{
                            drawn++;
                            System.out.println("Match Drawn");
                            fileWriter.append("Match Drawn\n");
                        }
                        break;
                    }
                }
                depth++;
                children.clear();
            }
            double winPercentage1=player1Win*100.0/(player1Win+player2Win);
            double winPercentage2=player2Win*100.0/(player1Win+player2Win);
            System.out.println("Player1: "+player1Win+" wins. "+"("+winPercentage1+"%)"+" Player2: "+player2Win+" wins. "+"("+winPercentage2+"%)"+" Drawn: "+drawn);
            fileWriter.append("Using Huristic "+count+": Player1: "+player1Win+" wins. "+"("+winPercentage1+"%)"+" Player2: "+player2Win+" wins. "+"("+winPercentage2+"%)"+" Drawn: "+drawn+"\n");
            player1Win=player2Win=drawn=0;
            count++;
        }
        fileWriter.close();
    }
    public static void printBoard(int[] player1, int[] player2){
        System.out.println("Current Board position----------");
        System.out.println("|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|");
        System.out.println("|         |   "+player1[6]+"   |   "+player1[5]+"   |   "+player1[4]+"   |   "+player1[3]+"   |   "+player1[2]+"   |   "+player1[1]+"   |         |");
        System.out.println("|    "+player1[0]+"    |=======|=======|=======|=======|=======|=======|    "+player2[0]+"    |");
        System.out.println("|         |   "+player2[1]+"   |   "+player2[2]+"   |   "+player2[3]+"   |   "+player2[4]+"   |   "+player2[5]+"   |   "+player2[6]+"   |         |");
        System.out.println("|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|");
    }
    public static ArrayList<Board> getChildren(Board board,int turn, int choice){
        ArrayList<Board> boards = new ArrayList<>(100);
        ArrayList<Board> sortedBoards = new ArrayList<>(100);
        if(turn==1){
            for(int i=1;i<7;i++){
                if(board.getPlayer1()[i]!=0){
                    int[] p1 = new int[10],p2=new int[10];
                    for(int j=0;j<7;j++){
                        p1[j]=board.getPlayer1()[j];
                        p2[j]=board.getPlayer2()[j];
                    }
                    Board tempBoard = new Board(p1,p2,board.getFree());
                    boards.add(doMove(tempBoard,turn,i));
                }
            }
        }
        else{
            for(int i=1;i<7;i++){
                if(board.getPlayer2()[i]!=0){
                    int[] p1 = new int[10],p2=new int[10];
                    for(int j=0;j<7;j++){
                        p1[j]=board.getPlayer1()[j];
                        p2[j]=board.getPlayer2()[j];
                    }
                    Board tempBoard = new Board(p1,p2,board.getFree());
                    boards.add(doMove(tempBoard,turn,i));
                }
            }
        }
        int idx;
        if(turn==1){
            int i,j;
            for(i=0;i<boards.size()-1;i++){
                idx=i;
                for(j=i+1;j<boards.size();j++){
                    if(evaluate(boards.get(j),choice,turn)>evaluate(boards.get(idx),choice,turn)){
                        idx=j;
                    }
                }
                Board temp=boards.get(i);
                boards.set(i,boards.get(idx));
                boards.set(idx,temp);
            }
        }
        else{
            int i,j;
            for(i=0;i<boards.size()-1;i++){
                idx=i;
                for(j=i+1;j<boards.size();j++){
                    if(evaluate(boards.get(j),choice,turn)<evaluate(boards.get(idx),choice,turn)){
                        idx=j;
                    }
                }
                Board temp=boards.get(i);
                boards.set(i,boards.get(idx));
                boards.set(idx,temp);
            }
        }
        return boards;
    }
    public static Board doMove(Board board,int turn,int position){
        int NumOfballs;
        board.setFree(false);
        board.setCapture(false);
        if(turn==1){
            int currentBoard=1;
            NumOfballs=board.getPlayer1()[position];
            board.getPlayer1()[position]=0;
            for(int i=1;i<=NumOfballs;i++){
                position++;
                if(position<=6 && currentBoard==1){
                    if(board.getPlayer1()[position]==0 && (i==NumOfballs) && board.getPlayer2()[7-position]!=0){
                        board.setCapture(true);
                        board.setExtra(1+board.getPlayer2()[7-position]);
                        board.getPlayer1()[0]+=1+board.getPlayer2()[7-position];
                        board.getPlayer2()[7-position]=0;
                    }
                    else{
                        board.getPlayer1()[position]+=1;
                    }
                }
                else if(position==7 && currentBoard==1){
                    board.getPlayer1()[0]+=1;
                    currentBoard=2;
                    position=0;
                    board.setOverflow(NumOfballs-i);
                    if(i==NumOfballs){
                        board.setFree(true);
                    }
                }
                else if(position<=6 && currentBoard==2){
                    board.getPlayer2()[position]+=1;
                }
                else if(position==7 && currentBoard==2){
                    position=0;
                    currentBoard=1;
                    i--;
                }
            }
        }
        else{
            int currentBoard=2;
            NumOfballs=board.getPlayer2()[position];
            board.getPlayer2()[position]=0;
            for(int i=1;i<=NumOfballs;i++){
                position++;
                if(position<=6 && currentBoard==2){
                    if(board.getPlayer2()[position]==0 && (i==NumOfballs) && board.getPlayer1()[7-position]!=0){
                        board.setCapture(true);
                        board.setExtra(1+board.getPlayer1()[7-position]);
                        board.getPlayer2()[0]+=1+board.getPlayer1()[7-position];
                        board.getPlayer1()[7-position]=0;
                    }
                    else{
                        board.getPlayer2()[position]+=1;
                    }
                }
                else if(position==7 && currentBoard==2){
                    board.getPlayer2()[0]+=1;
                    currentBoard=1;
                    position=0;
                    board.setOverflow(NumOfballs-i);
                    if(i==NumOfballs){
                        board.setFree(true);
                    }
                }
                else if(position<=6 && currentBoard==1){
                    board.getPlayer1()[position]+=1;
                }
                else if(position==7 && currentBoard==1){
                    position=0;
                    currentBoard=2;
                    i--;
                }
            }
        }
        return board;
    }
    public static double evaluate(Board board,int choice,int turn){
        int store1=board.getPlayer1()[0];
        int store2=board.getPlayer2()[0];
        int balls1=0;
        int balls2=0;
        int extra=0;
        if(board.getFree())
            extra=1;
        for(int i=1;i<7;i++){
            balls1+=board.getPlayer1()[i];
            balls2+=board.getPlayer2()[i];
        }
        double H1=store1-store2;
        double H2=13*H1+(balls1-balls2);
        double H3;
        if(turn==1)
            H3=H2+30*extra;
        else
            H3=H2-30*extra;
        double H5=-8*store2;
        double H6=store2-store1;
        double H7=H1;
        double H8=2*store1-((board.getOverflow()*0.3)*(board.getOverflow()*0.3))+4*board.getExtra();
        double H9=H1+H2+2*H3+H5+H6+H7+H8;
        if(store2>=5)
            H6=(store2*(-16.5))-store1;
        if(store1>=5)
            H7=store1*1.5-store2;
        if(choice==1){
            return H1;
        }
        else if(choice==2){
            return H2;
        }
        else if(choice==3){
            return H3;
        }
        else if(choice==4){
            return H5;
        }
        else if(choice==5){
            return H6;
        }
        else if(choice==6){
            return H7;
        }
        else if(choice==7){
            return H8;
        }
        else{
            return H9;
        }
    }
    public static double alphaBeta(Board board, int turn, double alpha, double beta, int depth, int choice, int who){
        if(depth==0){
            //printBoard(board.getPlayer1(),board.getPlayer2());
            return evaluate(board, choice, who);
        }
        if(turn==1){ //Maximize
            double bestValue = -9999;
            ArrayList<Board> boards = getChildren(board,turn,choice);
            for(int i=0;i<boards.size();i++){
                double value;
                if(boards.get(i).getFree()){
                    value=alphaBeta(boards.get(i),1,alpha,beta,depth-1,choice,who);
                    boards.get(i).setHval(value);
                }
                else{
                    value=alphaBeta(boards.get(i),2,alpha,beta,depth-1,choice,who);
                    boards.get(i).setHval(value);
                }
                bestValue = Math.max(bestValue,value);
                alpha=Math.max(alpha,bestValue);
                if(beta<=alpha) //pruning
                    break;
            }
            children=boards;
            return bestValue;
        }
        else{
            double bestValue=9999;
            ArrayList<Board> boards = getChildren(board,turn,choice);
            for(int i=0;i<boards.size();i++){
                double value;
                if(boards.get(i).getFree()){
                    value=alphaBeta(boards.get(i),2,alpha,beta,depth-1,choice,who);
                    boards.get(i).setHval(value);
                }
                else{
                    value=alphaBeta(boards.get(i),1,alpha,beta,depth-1,choice,who);
                    boards.get(i).setHval(value);
                }
                bestValue = Math.min(bestValue,value);
                beta = Math.min(beta,bestValue);
                if(beta<=alpha)
                    break;
            }
            children=boards;
            return bestValue;
        }
    }
    public static boolean EndGame(Board board){
        boolean isEnd=false;
        int isZero=0;
        for(int i=1;i<7;i++){
            if(board.getPlayer1()[i]==0){
                isZero++;
            }
        }
        if(isZero==6){
            isEnd=true;
            return isEnd;
        }
        isZero=0;
        for(int i=1;i<7;i++){
            if(board.getPlayer2()[i]==0){
                isZero++;
            }
        }
        if(isZero==6){
            isEnd=true;
            return isEnd;
        }
        return isEnd;
    }
}
