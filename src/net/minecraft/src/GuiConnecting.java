package net.minecraft.src;

import java.io.PrintStream;
import java.util.List;
import net.minecraft.client.Minecraft;

//====================
// START MODOPTIONSAPI
//====================
import moapi.ModOptionsAPI;
import moapi.ModOptions;
//====================
// END MODOPTIONSAPI
//====================
public class GuiConnecting extends GuiScreen
{
    private NetClientHandler clientHandler;
    private boolean cancelled;

    public GuiConnecting(Minecraft minecraft, String s, int i)
    {
		//====================
		// START MODOPTIONSAPI
		//====================
		ModOptionsAPI.joinedMultiplayerWorld(s);
		//====================
		// END MODOPTIONSAPI
		//====================
        cancelled = false;
        System.out.println((new StringBuilder()).append("Connecting to ").append(s).append(", ").append(i).toString());
        minecraft.changeWorld1(null);
        (new ThreadConnectToServer(this, minecraft, s, i)).start();
    }

    public void updateScreen()
    {
        if (clientHandler != null)
        {
            clientHandler.processReadPackets();
        }
    }

    protected void keyTyped(char c, int i)
    {
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        controlList.clear();
        controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120 + 12, stringtranslate.translateKey("gui.cancel")));
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if (guibutton.id == 0)
        {
            cancelled = true;
            if (clientHandler != null)
            {
                clientHandler.disconnect();
            }
            mc.displayGuiScreen(new GuiMainMenu());
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        StringTranslate stringtranslate = StringTranslate.getInstance();
        if (clientHandler == null)
        {
            drawCenteredString(fontRenderer, stringtranslate.translateKey("connect.connecting"), width / 2, height / 2 - 50, 0xffffff);
            drawCenteredString(fontRenderer, "", width / 2, height / 2 - 10, 0xffffff);
        }
        else
        {
            drawCenteredString(fontRenderer, stringtranslate.translateKey("connect.authorizing"), width / 2, height / 2 - 50, 0xffffff);
            drawCenteredString(fontRenderer, clientHandler.field_1209_a, width / 2, height / 2 - 10, 0xffffff);
        }
        super.drawScreen(i, j, f);
    }

    static NetClientHandler setNetClientHandler(GuiConnecting guiconnecting, NetClientHandler netclienthandler)
    {
        return guiconnecting.clientHandler = netclienthandler;
    }

    static boolean isCancelled(GuiConnecting guiconnecting)
    {
        return guiconnecting.cancelled;
    }

    static NetClientHandler getNetClientHandler(GuiConnecting guiconnecting)
    {
        return guiconnecting.clientHandler;
    }
}
