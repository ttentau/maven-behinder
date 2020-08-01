//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.rebeyond.behinder.ui;

import net.rebeyond.behinder.utils.Constants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;

public class CustomTable extends Table {
    public CustomTable(Composite arg0, int arg1, int menuStyle) {
        super(arg0, arg1);
        addContextMenu(this, menuStyle);
    }

    public static void addContextMenu(final Table control, int menuStyle) {
        Menu menu = new Menu(control);
        MenuItem item;
        if ((menuStyle & Constants.MENU_CUT) > 0) {
            item = new MenuItem(menu, 8);
            item.setText("剪切");
            item.addListener(13, new Listener() {
                public void handleEvent(Event event) {
                }
            });
        }

        if ((menuStyle & Constants.MENU_COPY) > 0) {
            item = new MenuItem(menu, 8);
            item.setText("复制");
            item.addListener(13, new Listener() {
                public void handleEvent(Event event) {
                }
            });
        }

        if ((menuStyle & Constants.MENU_PASTE) > 0) {
            item = new MenuItem(menu, 8);
            item.setText("粘贴");
            item.addListener(13, new Listener() {
                public void handleEvent(Event event) {
                }
            });
        }

        if ((menuStyle & Constants.MENU_SELECT_ALL) > 0) {
            item = new MenuItem(menu, 8);
            item.setText("全选");
            item.addListener(13, new Listener() {
                public void handleEvent(Event event) {
                    control.selectAll();
                }
            });
        }

        if ((menuStyle & Constants.MENU_CLEAR) > 0) {
            item = new MenuItem(menu, 8);
            item.setText("清空");
            item.addListener(13, new Listener() {
                public void handleEvent(Event event) {
                }
            });
        }

        control.setMenu(menu);
    }
}
