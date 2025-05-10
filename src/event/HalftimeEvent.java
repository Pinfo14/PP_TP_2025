package event;

public class HalftimeEvent extends Event {
    private static final String DESCRIPTION = "Intervalo ";
    public HalftimeEvent( int minute) {
        super(DESCRIPTION, minute);
    }
}
