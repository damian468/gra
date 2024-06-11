package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player(0, 0);

    }

    @Test
    void testHealth() {
        assertEquals(100, player.getHealth());
    }

    @Test
    void testAddHealth() {
        player.reduceHealth(50);
        player.addHealth(30);
        assertEquals(80, player.getHealth());
    }
    @Test
    void testDecreaseScore() {
        player.getScore().addPoints(50);
        player.decreaseScore(20);
        assertEquals(30, player.getScore().getPoints());
        player.decreaseScore(40);
        assertEquals(0, player.getScore().getPoints());
    }

}    