package com.example.space68.data;

import com.example.space68.model.CrewMember;
import com.example.space68.model.Threat;


// runs one mission: holds state, processes player actions, returns log lines
public class MissionManager {

    public enum Action { ATTACK, DEFEND, SPECIAL }
    public enum MissionState { IN_PROGRESS, VICTORY, DEFEAT }

    private CrewMember crewA;
    private CrewMember crewB;
    private Threat threat;
    private boolean aTurn; // true = A's turn, false = B's turn
    private MissionState state;
    private int round;

    public MissionManager(CrewMember crewA, CrewMember crewB, Threat threat) {
        this.crewA = crewA;
        this.crewB = crewB;
        this.threat = threat;
        this.aTurn = true;
        this.state = MissionState.IN_PROGRESS;
        this.round = 1;
    }

    // returns the crew member whose turn it is (null if mission over)
    public CrewMember getCurrentCrew() {
        if (state != MissionState.IN_PROGRESS) return null;
        if (aTurn && crewA.isAlive()) return crewA;
        if (!aTurn && crewB.isAlive()) return crewB;
        // current side is dead — try the other
        if (crewA.isAlive()) return crewA;
        if (crewB.isAlive()) return crewB;
        return null;
    }

    // process one action, returns the log text for it
    public String processAction(Action action) {
        StringBuilder log = new StringBuilder();
        CrewMember actor = getCurrentCrew();
        if (actor == null || state != MissionState.IN_PROGRESS) {
            return "";
        }

        if (aTurn && actor == crewA && round > 1 || !aTurn && actor == crewB) {
            // normal flow
        }

        // header on first action of each round
        if (aTurn || (!crewA.isAlive())) {
            // start of a round if we're back to A (or A is dead and B leads)
        }

        // calculate damage based on action
        int damage;
        boolean defending = false;
        java.util.Random rand = new java.util.Random();
        switch (action) {
            case ATTACK:
                damage = actor.atkAgainst(threat) + rand.nextInt(3);
                break;
            case DEFEND:
                damage = 0;
                defending = true;
                break;
            case SPECIAL:
                damage = (int) (actor.atkAgainst(threat) * 1.5) + rand.nextInt(3);
                break;
            default:
                damage = actor.atkAgainst(threat);
        }

        // crew acts on threat
        int dealt = Math.max(0, damage - threat.getResilience());
        threat.defend(damage);
        log.append(actor.getSpecialization()).append("(").append(actor.getName()).append(") ");
        if (action == Action.DEFEND) {
            log.append("defends this turn\n");
        } else if (action == Action.SPECIAL) {
            log.append("uses SPECIAL on ").append(threat.getName()).append("\n");
            log.append("  damage dealt: ").append(damage).append(" - ").append(threat.getResilience()).append(" = ").append(dealt).append("\n");
        } else {
            log.append("attacks ").append(threat.getName()).append("\n");
            log.append("  damage dealt: ").append(damage).append(" - ").append(threat.getResilience()).append(" = ").append(dealt).append("\n");
        }
        log.append("  ").append(threat.getName()).append(" energy: ").append(threat.getEnergy()).append("/").append(threat.getMaxEnergy()).append("\n");

        // check threat death
        if (threat.isDefeated()) {
            state = MissionState.VICTORY;
            log.append("\n=== MISSION COMPLETE ===\n");
            log.append("The ").append(threat.getName()).append(" has been neutralized!\n");
            if (crewA.isAlive()) {
                crewA.gainExperience(1);
                crewA.recordMission(true);
                log.append(crewA.getName()).append(" gains 1 XP (total: ").append(crewA.getExperience()).append(")\n");
            }
            if (crewB.isAlive()) {
                crewB.gainExperience(1);
                crewB.recordMission(true);
                log.append(crewB.getName()).append(" gains 1 XP (total: ").append(crewB.getExperience()).append(")\n");
            }
            return log.toString();
        }

        // threat retaliates
        int threatDmg = threat.atk() + rand.nextInt(3);
        if (defending) threatDmg = threatDmg / 2; // defend halves incoming
        int actualDmg = Math.max(0, threatDmg - actor.getResilience());
        actor.defend(threatDmg);
        log.append(threat.getName()).append(" retaliates against ").append(actor.getName()).append("\n");
        log.append("  damage dealt: ").append(threatDmg).append(" - ").append(actor.getResilience()).append(" = ").append(actualDmg).append("\n");
        log.append("  ").append(actor.getName()).append(" energy: ").append(actor.getEnergy()).append("/").append(actor.getMaxEnergy()).append("\n");

        if (!actor.isAlive()) {
            log.append(actor.getName()).append(" has fallen!\n");
        }

        // check defeat
        if (!crewA.isAlive() && !crewB.isAlive()) {
            state = MissionState.DEFEAT;
            log.append("\n=== MISSION FAILED ===\n");
            log.append("All crew members lost.\n");
            crewA.recordMission(false);
            crewB.recordMission(false);
            return log.toString();
        }

        // swap turn
        aTurn = !aTurn;
        if (aTurn) round++;

        return log.toString();
    }

    public MissionState getState() { return state; }
    public CrewMember getCrewA() { return crewA; }
    public CrewMember getCrewB() { return crewB; }
    public Threat getThreat() { return threat; }
    public int getRound() { return round; }
}