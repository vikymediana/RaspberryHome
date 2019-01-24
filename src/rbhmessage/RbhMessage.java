package rbhmessage;

import java.io.Serializable;

public class RbhMessage implements Serializable {

    public enum MessageType {
        CREATE,
        DELETE,
        ORDER,
        DATA
    }

    private MessageType messageType;
    private Item item;
    private String itemId;
    private byte[] data;


    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
