package net.rev.stealthandalert.attachment;

public record VisibilityData(float visibility) {
    public static final VisibilityData DEFAULT = new VisibilityData(1.0F);
}
