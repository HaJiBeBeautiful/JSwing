package sd.jswing.pro.common;

import java.awt.Component;

import javax.swing.JOptionPane;

public class DialogUtils {
	/**
	 * 确认消息
	 * @param message
	 * @param parent
	 * @return
	 */
	public static int showConfirmDialog(String message,Component parent) {
		return JOptionPane.showConfirmDialog(parent, message, "提示", JOptionPane.YES_NO_OPTION);
	}
	/**
	 * 提示消息
	 * @param message
	 * @param parent
	 */
	public static void alertMessage(String message,Component parent) {
		JOptionPane.showMessageDialog(
				parent,
				message,
                "消息提示",
                JOptionPane.WARNING_MESSAGE
        );
	}
}
