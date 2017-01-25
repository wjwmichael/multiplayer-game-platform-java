package jw84_pc30_chatApp.util;

import common.msg.chat.ITextMsg;

public class TextMsg implements ITextMsg{

	String content;
	/**
	 * 
	 */
	private static final long serialVersionUID = -8844240090150913865L;
	public TextMsg(String text){
		content = text;
	}
	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return content;
	}

}
