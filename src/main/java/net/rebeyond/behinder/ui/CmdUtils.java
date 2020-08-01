//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.rebeyond.behinder.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.rebeyond.behinder.core.ShellService;
import net.rebeyond.behinder.utils.Constants;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.json.JSONObject;

public class CmdUtils {
    public int currentPos;
    private int running;
    private List<String> cmdHistory;
    private List<String> realCmdHistory;
    private ShellService currentShellService;
    private Label statusLabel;
    private JSONObject shellEntity;

    public CmdUtils(ShellService shellService, Label statusLabel, JSONObject shellEntity) {
        this.running = Constants.REALCMD_STOPPED;
        this.cmdHistory = new ArrayList();
        this.realCmdHistory = new ArrayList();
        this.currentShellService = shellService;
        this.statusLabel = statusLabel;
        this.shellEntity = shellEntity;
    }

    public void sendCommand(KeyEvent e, Text cmdView) {
        MainShell mainShell = (MainShell)cmdView.getShell();
        Map<String, String> basicInfo = mainShell.basicInfoMap;
        if ((e.keyCode == 8 || e.keyCode == 16777219) && cmdView.getCaretPosition() <= this.currentPos) {
            e.doit = false;
        }

        if (e.keyCode == 16777217) {
            e.doit = false;
        }

        if (cmdView.getCaretPosition() < this.currentPos) {
            e.doit = false;
        }

        if (e.keyCode == 13) {
            String pwd = (String)basicInfo.get("currentPath") + " >";

            try {
                int lines = cmdView.getText().split("\n").length;
                String lastLine = cmdView.getText().split("\n")[lines - 1];
                String cmd = lastLine.substring(lastLine.indexOf(pwd) + pwd.length());
                JSONObject resultObj = this.currentShellService.runCmd(cmd);
                if (resultObj.getString("status").equals("success")) {
                    cmdView.insert("\n" + resultObj.getString("msg") + "\n");
                    cmdView.insert(pwd);
                    this.statusLabel.setText("命令执行完成");
                    this.currentPos = cmdView.getCaretPosition();
                } else {
                    cmdView.insert("\n" + resultObj.getString("msg") + "\n");
                    cmdView.insert(pwd);
                    this.statusLabel.setText("命令执行失败:" + resultObj.getString("msg"));
                }

                e.doit = false;
            } catch (Exception var10) {
                e.doit = false;
                var10.printStackTrace();
                this.statusLabel.setText(var10.getMessage());
                var10.printStackTrace();
            }
        }

    }

    public void createRealCMD(final StyledText cmdView, final String imagePath) throws Exception {
        this.statusLabel.setText("正在启动虚拟终端……");
        (new Thread() {
            public void run() {
                try {
                    final String bashPath = imagePath;
                    (new Thread() {
                        public void run() {
                            try {
                                CmdUtils.this.currentShellService.createRealCMD(bashPath);
                            } catch (Exception var2) {
                                var2.printStackTrace();
                            }

                        }
                    }).start();
                    Thread.sleep(1000L);

                    JSONObject resultObj;
                    for(resultObj = CmdUtils.this.currentShellService.readRealCMD(); resultObj.getString("status").equals("success") && resultObj.getString("msg").equals(""); resultObj = CmdUtils.this.currentShellService.readRealCMD()) {
                    }

                    final String status = resultObj.getString("status");
                    final String msg = resultObj.getString("msg");
                    Display.getDefault().syncExec(new Runnable() {
                        public void run() {
                            if (!CmdUtils.this.statusLabel.isDisposed()) {
                                if (status.equals("success")) {
                                    cmdView.setForeground(Display.getDefault().getSystemColor(5));
                                    cmdView.append(msg);
                                    cmdView.setCaretOffset(cmdView.getCharCount());
                                    CmdUtils.this.currentPos = cmdView.getCaretOffset();
                                    cmdView.setFocus();
                                    CmdUtils.this.statusLabel.setText("虚拟终端启动完成。");
                                    CmdUtils.this.running = Constants.REALCMD_RUNNING;
                                } else {
                                    CmdUtils.this.statusLabel.setText("虚拟终端启动失败:" + msg);
                                }

                            }
                        }
                    });
                } catch (Exception var5) {
                    var5.printStackTrace();
                    Display.getDefault().syncExec(new Runnable() {
                        public void run() {
                            if (!CmdUtils.this.statusLabel.isDisposed()) {
                                CmdUtils.this.statusLabel.setText("虚拟终端启动失败");
                            }
                        }
                    });
                }

            }
        }).start();
    }

    public void stopRealCMD(StyledText cmdView, String imagePath) throws Exception {
        this.statusLabel.setText("正在停止虚拟终端……");
        (new Thread() {
            public void run() {
                try {
                    JSONObject resultObj = CmdUtils.this.currentShellService.stopRealCMD();
                    final String status = resultObj.getString("status");
                    final String msg = resultObj.getString("msg");
                    Display.getDefault().syncExec(new Runnable() {
                        public void run() {
                            if (!CmdUtils.this.statusLabel.isDisposed()) {
                                if (status.equals("success")) {
                                    CmdUtils.this.statusLabel.setText("虚拟终端已停止。");
                                    CmdUtils.this.running = Constants.REALCMD_STOPPED;
                                } else {
                                    CmdUtils.this.statusLabel.setText("虚拟终端启动失败:" + msg);
                                }

                            }
                        }
                    });
                } catch (Exception var4) {
                    var4.printStackTrace();
                    Display.getDefault().syncExec(new Runnable() {
                        public void run() {
                            if (!CmdUtils.this.statusLabel.isDisposed()) {
                                CmdUtils.this.statusLabel.setText("虚拟停止启动失败");
                            }
                        }
                    });
                }

            }
        }).start();
    }

    public void filterEvent(StyledText cmdView, KeyEvent key) {
        char keyValue = key.character;
        if ((keyValue == '\b' || key.keyCode == 16777219) && cmdView.getCaretOffset() <= this.currentPos) {
            key.doit = false;
        }

        if (key.keyCode == 16777217) {
            key.doit = false;
        }

        if (cmdView.getCaretOffset() < this.currentPos) {
            if (keyValue != '\r') {
                key.doit = false;
            } else {
                cmdView.setCaretOffset(cmdView.getCharCount());
            }
        }

    }

    public void runRealCMD(final StyledText cmdView, final KeyEvent key) throws Exception {
        if (this.running != Constants.REALCMD_RUNNING) {
            this.statusLabel.setText("虚拟终端尚未启动，请先启动虚拟终端。");
        } else {
            final char keyValue = key.character;
            if (keyValue == '\t' || keyValue == '\r') {
                final String cmd = cmdView.getText(this.currentPos, cmdView.getCaretOffset() - 1).trim();
                this.statusLabel.setText("请稍后……");
                (new Thread() {
                    public void run() {
                        try {
                            String result = "";
                            if (keyValue == '\r') {
                                key.doit = false;
                                CmdUtils.this.currentShellService.writeRealCMD(cmd + "\n");
                                Thread.sleep(1000L);
                                JSONObject resultObj = CmdUtils.this.currentShellService.readRealCMD();
                                String status = resultObj.getString("status");
                                String msg = resultObj.getString("msg");
                                result = msg;
                                if (msg.length() > 1) {
                                    if (msg.startsWith(cmd, 0)) {
                                        result = msg.substring(cmd.length(), msg.length());
                                    }

                                    result = result.startsWith("\n") ? result : "\n" + result;
                                    result = result.startsWith("\n") ? result.substring(1) : result;
                                    final String finalResult = result;
                                    Display.getDefault().syncExec(new Runnable() {
                                        public void run() {
                                            if (!CmdUtils.this.statusLabel.isDisposed()) {
                                                cmdView.append(finalResult);
                                                cmdView.setCaretOffset(cmdView.getCharCount());
                                                cmdView.setTopIndex(cmdView.getLineCount() - 1);
                                                CmdUtils.this.currentPos = cmdView.getCaretOffset();
                                                CmdUtils.this.statusLabel.setText("完成。");
                                            }
                                        }
                                    });
                                }
                            }
                        } catch (Exception var6) {
                            var6.printStackTrace();
                        }

                    }
                }).start();
            }
        }
    }
}
