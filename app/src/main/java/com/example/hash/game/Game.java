package com.example.hash.game;

public class Game {

    private String game[][];
    private int num, counter;
    private int fisrt;
    private int second;

    public Game(){
        game = new String[3][3];
        num = -1;
        fisrt = 0;
        second = 0;
        counter = 0;
    }

    public int getFisrt() {
        return fisrt;
    }

    public void setFisrt(int fisrt) {
        this.fisrt = fisrt;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int checking(){
        //jogador 1
        // verificando se o jogador(x) ganhou pela horizontal
        if("x".equals(game[0][0]) && "x".equals(game[0][1]) && "x".equals(game[0][2])){
            return 0;
        }

        else if("x".equals(game[1][0]) && "x".equals(game[1][1]) && "x".equals(game[1][2])){
            return 0;
        }

        else if("x".equals(game[2][0]) && "x".equals(game[2][1]) && "x".equals(game[2][2])){
            return 0;
        }

        // verificando se o jogador(x) ganhou pela vertical
        else if("x".equals(game[0][0]) && "x".equals(game[1][0]) && "x".equals(game[2][0])){
            return 0;
        }

        else if("x".equals(game[0][1]) && "x".equals(game[1][1]) && "x".equals(game[2][1])){
            return 0;
        }

        else if("x".equals(game[0][2]) && "x".equals(game[1][2]) && "x".equals(game[2][2])){
            return 0;
        }

        // verificando se o jogador(x) ganhou pela diagonal

        else if("x".equals(game[0][0]) && "x".equals(game[1][1]) && "x".equals(game[2][2])){
            return 0;
        }

        else if("x".equals(game[0][2]) && "x".equals(game[1][1]) && "x".equals(game[2][0])){
            return 0;
        }

        // Jogador 2
        // verificando se o jogador(o) ganhou pela horizontal

        if("o".equals(game[0][0]) && "o".equals(game[0][1]) && "o".equals(game[0][2])){
            return 1;
        }

        else if("o".equals(game[1][0]) && "o".equals(game[1][1]) && "o".equals(game[1][2])){
            return 1;
        }

        else if("o".equals(game[2][0]) && "o".equals(game[2][1]) && "o".equals(game[2][2])){
            return 1;
        }

        // verificando se o jogador(x) ganhou pela vertical
        else if("o".equals(game[0][0]) && "o".equals(game[1][0]) && "o".equals(game[2][0])){
            return 1;
        }

        else if("o".equals(game[0][1]) && "o".equals(game[1][1]) && "o".equals(game[2][1])){
            return 1;
        }

        else if("o".equals(game[0][2]) && "o".equals(game[1][2]) && "o".equals(game[2][2])){
            return 1;
        }

        // verificando se o jogador(x) ganhou pela diagonal
        else if("o".equals(game[0][0]) && "o".equals(game[1][1]) && "o".equals(game[2][2])){
            return 1;
        }

        else if("o".equals(game[0][2]) && "o".equals(game[1][1]) && "o".equals(game[2][0])){
            return 1;
        }
        counter += 1;
        if(counter == 27){
            return 2;
        }
        return 3;
    }

    public boolean turn(){
        num = num + 1;

        if (num % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void toCheck(int one, int two){
        if (turn()) {
            game[one][two] = "x";
        } else {
            game[one][two] = "o";
        }
        num = num + 1;
    }
}
