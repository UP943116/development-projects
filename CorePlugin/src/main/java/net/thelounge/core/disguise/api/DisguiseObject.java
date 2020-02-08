package net.thelounge.core.disguise.api;

public class DisguiseObject {

    private String name;
    private String value;
    private String signature;

    public DisguiseObject(String name, String value, String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public String getSignature() {
        return this.signature;
    }
}
