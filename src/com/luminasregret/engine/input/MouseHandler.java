package com.luminasregret.engine.input;

import com.luminasregret.engine.core.GamePanel;
import com.luminasregret.game.entity.Entity;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter {

    private final GamePanel gp;
    public int mouseX, mouseY;
    public volatile boolean mousePressed = false;

    public MouseHandler(GamePanel gp) {
        this.gp = gp;
    }

    private int getScaledX(int x) {
        if (gp.screenWidth2 <= 0) return x;
        return (int) ((double) x * gp.screenWidth / gp.screenWidth2);
    }

    private int getScaledY(int y) {
        if (gp.screenHeight2 <= 0) return y;
        return (int) ((double) y * gp.screenHeight / gp.screenHeight2);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.mouseX = getScaledX(e.getX());
        this.mouseY = getScaledY(e.getY());
        this.mousePressed = true;

        handleMousePress(e, this.mouseX, this.mouseY);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.mousePressed = false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mouseX = getScaledX(e.getX());
        this.mouseY = getScaledY(e.getY());
        handleMouseHover(this.mouseX, this.mouseY);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.mouseX = getScaledX(e.getX());
        this.mouseY = getScaledY(e.getY());
        if (gp.gameState == gp.optionsState && gp.ui.subState == 0) {
            handleOptionsClick(this.mouseX, this.mouseY);
        }
    }

    private void handleMousePress(MouseEvent e, int x, int y) {
        // 1. PLAY STATE
        if (gp.gameState == gp.playState) {
            int worldMouseX = x + gp.player.worldX - gp.player.screenX;
            int worldMouseY = y + gp.player.worldY - gp.player.screenY;

            if (e.getButton() == MouseEvent.BUTTON1) {
                // Coba interaksi klik mouse (NPC, Chest, Door, Tree)
                boolean interacted = gp.player.interactAtLocation(worldMouseX, worldMouseY);
                if (!interacted) {
                    // Serang ke arah mouse
                    gp.player.attackTowards(worldMouseX, worldMouseY);
                }
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                // Klik kanan: Dash ke arah mouse jika cooldown selesai
                if (gp.player.dashCoolDown == 0 && !gp.player.attackCanceled) {
                    gp.player.faceTowards(worldMouseX, worldMouseY);
                    gp.player.dashing = true;
                    gp.playSE(7);
                }
            }
        }
        // 2. DIALOGUE STATE (Klik di manapun untuk lanjut dialog)
        else if (gp.gameState == gp.dialogueState) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                gp.advanceDialogue();
            }
        }
        // 3. TITLE STATE
        else if (gp.gameState == gp.titleState) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                int startY = (int) (gp.tileSize * 6.5);
                if (y >= startY - 30 && y <= startY + 20) {
                    gp.ui.commandNum = 0; // NEW GAME
                    gp.gameState = gp.playState;
                    gp.playMusic(0);
                } else if (y >= startY + 20 && y <= startY + 70) {
                    gp.ui.commandNum = 1; // LOAD GAME
                } else if (y >= startY + 70 && y <= startY + 120) {
                    gp.ui.commandNum = 2; // QUIT
                    System.exit(0);
                }
            }
        }
        // 4. GAME OVER STATE
        else if (gp.gameState == gp.gameOverState) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                int retryY = gp.tileSize * 6;
                if (y >= retryY - 30 && y <= retryY + 30) {
                    gp.ui.commandNum = 0;
                    gp.gameState = gp.playState;
                    gp.resetGame(false);
                    gp.playAreaMusic();
                } else if (y >= retryY + 30 && y <= retryY + 90) {
                    gp.ui.commandNum = 1;
                    gp.gameState = gp.titleState;
                    gp.resetGame(true);
                    gp.playMusic(4);
                }
            }
        }
        // 5. GAME CLEAR STATE
        else if (gp.gameState == gp.gameClearState) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                int optY = (int)(gp.tileSize * 7.5);
                if (y >= optY - 30 && y <= optY + 25) {
                    gp.resetGame(true);
                    gp.gameState = gp.playState;
                    gp.playAreaMusic();
                } else if (y >= optY + 25 && y <= optY + 80) {
                    gp.resetGame(true);
                    gp.gameState = gp.titleState;
                    gp.playMusic(4);
                }
            }
        }
        // 6. TRADE STATE
        else if (gp.gameState == gp.tradeState) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (gp.ui.subState == 0) {
                    int menuX = gp.tileSize * 12;
                    int menuY = gp.tileSize * 3;
                    if (x >= menuX && x <= menuX + gp.tileSize * 3) {
                        if (y >= menuY && y < menuY + gp.tileSize) {
                            gp.ui.commandNum = 0;
                            gp.ui.subState = 1; // Beli
                            gp.playSE(9);
                        } else if (y >= menuY + gp.tileSize && y < menuY + gp.tileSize * 2) {
                            gp.ui.commandNum = 1;
                            gp.ui.subState = 2; // Jual
                            gp.playSE(9);
                        } else if (y >= menuY + gp.tileSize * 2 && y <= menuY + gp.tileSize * 3 + 20) {
                            gp.ui.commandNum = 0;
                            gp.gameState = gp.playState; // Keluar
                            gp.ui.npc = null;
                            gp.ui.currentDialogue = "";
                            gp.ui.currentSpeakerName = "";
                            gp.playSE(9);
                        }
                    }
                }
                else if (gp.ui.subState == 1) { // Beli
                    // Klik slot item NPC
                    int frameX = gp.tileSize - 12;
                    int slotXstart = frameX + 20;
                    int slotYstart = gp.tileSize - 4;
                    int slotSize = gp.tileSize + 3;
                    if (x >= slotXstart && x < slotXstart + slotSize * 5 && y >= slotYstart && y < slotYstart + slotSize * 4) {
                        int col = (x - slotXstart) / slotSize;
                        int row = (y - slotYstart) / slotSize;
                        gp.ui.npcSlotCol = col;
                        gp.ui.npcSlotRow = row;
                        gp.keyH.actionPressed = true;
                    }
                    // Klik tombol [ESC] Kembali
                    int hintY = gp.tileSize * 8;
                    if (x >= gp.tileSize * 9 + 14 && x <= gp.tileSize * 15 && y >= hintY - gp.tileSize * 3 + 22 && y <= hintY - gp.tileSize * 2 + 22) {
                        gp.ui.subState = 0;
                        gp.playSE(9);
                    }
                }
                else if (gp.ui.subState == 2) { // Jual
                    // Klik slot item Player
                    int frameX = gp.tileSize * 9 + 12;
                    int slotXstart = frameX + 20;
                    int slotYstart = gp.tileSize - 4;
                    int slotSize = gp.tileSize + 3;
                    if (x >= slotXstart && x < slotXstart + slotSize * 5 && y >= slotYstart && y < slotYstart + slotSize * 4) {
                        int col = (x - slotXstart) / slotSize;
                        int row = (y - slotYstart) / slotSize;
                        gp.ui.playerSlotCol = col;
                        gp.ui.playerSlotRow = row;
                        gp.keyH.actionPressed = true;
                    }
                    // Klik tombol [ESC] Kembali
                    int hintX = gp.tileSize * 2;
                    int hintY = gp.tileSize * 7;
                    if (x >= hintX && x <= hintX + gp.tileSize * 6 && y >= hintY && y <= hintY + gp.tileSize * 2) {
                        gp.ui.subState = 0;
                        gp.playSE(9);
                    }
                }
            }
        }
        // 7. PAUSE STATE (Klik untuk resume)
        else if (gp.gameState == gp.pauseState) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                gp.gameState = gp.playState;
            }
        }
        // 8. CHARACTER STATE
        else if (gp.gameState == gp.characterState) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                int frameX = gp.tileSize * 9 + 12;
                int slotXstart = frameX + 20;
                int slotYstart = gp.tileSize - 4;
                int slotSize = gp.tileSize + 3;
                if (x >= slotXstart && x < slotXstart + slotSize * 5 && y >= slotYstart && y < slotYstart + slotSize * 4) {
                    int col = (x - slotXstart) / slotSize;
                    int row = (y - slotYstart) / slotSize;
                    gp.ui.playerSlotCol = col;
                    gp.ui.playerSlotRow = row;
                    gp.player.selectItem();
                } else if (x < gp.tileSize - 20 || x > gp.tileSize * 15 + 30 || y < 10 || y > gp.tileSize * 8.5) {
                    gp.gameState = gp.playState;
                }
            }
        }
        // 9. OPTIONS STATE
        else if (gp.gameState == gp.optionsState) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                handleOptionsClick(x, y);
            }
        }
    }

    private void handleOptionsClick(int x, int y) {
        int frameWidth = gp.tileSize * 8;
        int frameHeight = gp.tileSize * 8;
        int frameX = (gp.screenWidth - frameWidth) / 2;
        int frameY = (gp.screenHeight - frameHeight) / 2;
        int itemDy = gp.tileSize - 10;
        int textX = frameX + gp.tileSize - 12;
        int controlX = frameX + (int)(gp.tileSize * 4.9);
        int startY = frameY + gp.tileSize * 2 + 10;

        if (gp.ui.subState == 0) {
            int r0Y = startY;
            int r1Y = startY + itemDy;
            int r2Y = startY + itemDy * 2;
            int r3Y = startY + itemDy * 3;
            int r4Y = startY + itemDy * 4;
            int r5Y = startY + itemDy * 6;

            // Full Screen toggle (Label atau Checkbox)
            if (y >= r0Y - 25 && y <= r0Y + 10 && ((x >= textX - 25 && x <= textX + 150) || (x >= controlX - 5 && x <= controlX + 35))) {
                gp.toggleFullScreen();
                gp.playSE(9);
            }
            // Music Volume Slider (klik/drag)
            else if (y >= r1Y - 25 && y <= r1Y + 10 && x >= controlX - 5 && x <= controlX + 130) {
                int scale = Math.max(0, Math.min(5, (x - controlX + 12) / 24));
                gp.music.volumeScale = scale;
                gp.music.checkVolume();
                gp.config.saveConfig();
            }
            // SE Volume Slider (klik/drag)
            else if (y >= r2Y - 25 && y <= r2Y + 10 && x >= controlX - 5 && x <= controlX + 130) {
                int scale = Math.max(0, Math.min(5, (x - controlX + 12) / 24));
                gp.se.volumeScale = scale;
                gp.playSE(9);
                gp.config.saveConfig();
            }
            // Control Button
            else if (y >= r3Y - 25 && y <= r3Y + 10 && x >= textX - 25 && x <= textX + 150) {
                gp.ui.subState = 2;
                gp.ui.commandNum = 0;
                gp.playSE(9);
            }
            // End Game Button
            else if (y >= r4Y - 25 && y <= r4Y + 10 && x >= textX - 25 && x <= textX + 150) {
                gp.ui.subState = 3;
                gp.ui.commandNum = 0;
                gp.playSE(9);
            }
            // Back Button
            else if (y >= r5Y - 25 && y <= r5Y + 10 && x >= textX - 25 && x <= textX + 150) {
                gp.gameState = gp.playState;
                gp.ui.commandNum = 0;
                gp.playSE(9);
            }
        }
        else if (gp.ui.subState == 2) {
            // Control screen: Back button
            int backX = frameX + gp.tileSize;
            int backY = frameY + gp.tileSize * 7 + 20;
            if (x >= backX - 30 && x <= backX + 120 && y >= backY - 25 && y <= backY + 25) {
                gp.ui.subState = 0;
                gp.ui.commandNum = 3;
                gp.playSE(9);
            }
        }
        else if (gp.ui.subState == 3) {
            // End Game confirmation: Yes or No
            int yesY = frameY + gp.tileSize * 3;
            int noY = frameY + gp.tileSize * 4;
            if (y >= yesY - 10 && y <= yesY + 30 && x >= gp.screenWidth/2 - 60 && x <= gp.screenWidth/2 + 60) {
                gp.ui.subState = 0;
                gp.gameState = gp.titleState;
                gp.stopMusic();
            } else if (y >= noY - 10 && y <= noY + 30 && x >= gp.screenWidth/2 - 60 && x <= gp.screenWidth/2 + 60) {
                gp.ui.subState = 0;
                gp.ui.commandNum = 4;
                gp.playSE(9);
            }
        }
    }

    private void handleMouseHover(int x, int y) {
        if (gp.gameState == gp.titleState) {
            int startY = (int) (gp.tileSize * 6.5);
            if (y >= startY - 30 && y <= startY + 20) {
                gp.ui.commandNum = 0;
            } else if (y >= startY + 20 && y <= startY + 70) {
                gp.ui.commandNum = 1;
            } else if (y >= startY + 70 && y <= startY + 120) {
                gp.ui.commandNum = 2;
            }
        } else if (gp.gameState == gp.gameOverState) {
            int retryY = gp.tileSize * 6;
            if (y >= retryY - 30 && y <= retryY + 30) {
                gp.ui.commandNum = 0;
            } else if (y >= retryY + 30 && y <= retryY + 90) {
                gp.ui.commandNum = 1;
            }
        } else if (gp.gameState == gp.optionsState && gp.ui.subState == 0) {
            int frameWidth = gp.tileSize * 8;
            int frameHeight = gp.tileSize * 8;
            int frameX = (gp.screenWidth - frameWidth) / 2;
            int frameY = (gp.screenHeight - frameHeight) / 2;
            int itemDy = gp.tileSize - 10;
            int textX = frameX + gp.tileSize - 12;
            int startY = frameY + gp.tileSize * 2 + 10;

            if (x >= textX - 25 && x <= textX + 250) {
                int r0Y = startY;
                int r1Y = startY + itemDy;
                int r2Y = startY + itemDy * 2;
                int r3Y = startY + itemDy * 3;
                int r4Y = startY + itemDy * 4;
                int r5Y = startY + itemDy * 6;

                if (y >= r0Y - 25 && y <= r0Y + 10) gp.ui.commandNum = 0;
                else if (y >= r1Y - 25 && y <= r1Y + 10) gp.ui.commandNum = 1;
                else if (y >= r2Y - 25 && y <= r2Y + 10) gp.ui.commandNum = 2;
                else if (y >= r3Y - 25 && y <= r3Y + 10) gp.ui.commandNum = 3;
                else if (y >= r4Y - 25 && y <= r4Y + 10) gp.ui.commandNum = 4;
                else if (y >= r5Y - 25 && y <= r5Y + 10) gp.ui.commandNum = 5;
            }
        } else if (gp.gameState == gp.characterState) {
            int frameX = gp.tileSize * 9 + 12;
            int slotXstart = frameX + 20;
            int slotYstart = gp.tileSize - 4;
            int slotSize = gp.tileSize + 3;
            if (x >= slotXstart && x < slotXstart + slotSize * 5 && y >= slotYstart && y < slotYstart + slotSize * 4) {
                gp.ui.playerSlotCol = (x - slotXstart) / slotSize;
                gp.ui.playerSlotRow = (y - slotYstart) / slotSize;
            }
        } else if (gp.gameState == gp.tradeState) {
            if (gp.ui.subState == 0) {
                int menuX = gp.tileSize * 12;
                int menuY = gp.tileSize * 3;
                if (x >= menuX && x <= menuX + gp.tileSize * 3) {
                    if (y >= menuY && y < menuY + gp.tileSize) {
                        gp.ui.commandNum = 0;
                    } else if (y >= menuY + gp.tileSize && y < menuY + gp.tileSize * 2) {
                        gp.ui.commandNum = 1;
                    } else if (y >= menuY + gp.tileSize * 2 && y <= menuY + gp.tileSize * 3 + 20) {
                        gp.ui.commandNum = 2;
                    }
                }
            } else if (gp.ui.subState == 1) { // Beli
                int frameX = gp.tileSize - 12;
                int slotXstart = frameX + 20;
                int slotYstart = gp.tileSize - 4;
                int slotSize = gp.tileSize + 3;
                if (x >= slotXstart && x < slotXstart + slotSize * 5 && y >= slotYstart && y < slotYstart + slotSize * 4) {
                    gp.ui.npcSlotCol = (x - slotXstart) / slotSize;
                    gp.ui.npcSlotRow = (y - slotYstart) / slotSize;
                }
            } else if (gp.ui.subState == 2) { // Jual
                int frameX = gp.tileSize * 9 + 12;
                int slotXstart = frameX + 20;
                int slotYstart = gp.tileSize - 4;
                int slotSize = gp.tileSize + 3;
                if (x >= slotXstart && x < slotXstart + slotSize * 5 && y >= slotYstart && y < slotYstart + slotSize * 4) {
                    gp.ui.playerSlotCol = (x - slotXstart) / slotSize;
                    gp.ui.playerSlotRow = (y - slotYstart) / slotSize;
                }
            }
        }
    }
}
