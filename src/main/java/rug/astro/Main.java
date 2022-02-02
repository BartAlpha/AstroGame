package rug.astro;

import rug.astro.model.Game;
import rug.astro.view.MainMenuFrame;

public class Main {
    public static void main(String[] args) {
        if (Game.SPACESIZE < 2000) {
            System.out.println("Please use a >=2000 spacesize");
        } else {
            MainMenuFrame frame = new MainMenuFrame();
        }


    }
}
