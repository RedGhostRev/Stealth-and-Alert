package net.rev.stealthandalert.common.animation;

import java.util.ArrayList;
import java.util.List;

public class AssassinationManager {
    private static final List<AssassinationSession> SERVER_SESSIONS = new ArrayList<>();
    private static final List<AssassinationSession> CLIENT_SESSIONS = new ArrayList<>();

    public static void startSession(AssassinationSession session) {
        if (session.isClientSide()) {
            CLIENT_SESSIONS.add(session);
        } else {
            SERVER_SESSIONS.add(session);
        }
    }

    public static void onServerGameTick() {
        tickList(SERVER_SESSIONS);
    }

    public static void onClientGameTick() {
        tickList(CLIENT_SESSIONS);
    }

    public static void tickList(List<AssassinationSession> sessions) {
        if (sessions.isEmpty()) return;
        for (int i = sessions.size() - 1; i >= 0; i--) {
            AssassinationSession session = sessions.get(i);
            session.tick();
            if (session.isFinished()) {
                sessions.remove(i);
            }
        }
    }
}
