import java.util.ArrayList;

public class Politics {
    ArrayList<int[]> attacks;
    int size;
    ArrayList<Voter> voters = new ArrayList<>();
    UnionFind set;


    public Politics(ArrayList<int[]> attacks, int size) {
        this.attacks = attacks;
        this.size = size;
        this.set = new UnionFind(size);

        for (int i = 0; i < size; i++) {
            voters.add(new Voter(i));
        }
    }

    /**
     * Handle all attacks on the class.
     */
    public void handleAttacks() {
        for (int[] attack: attacks) {
            int attacker = attack[0] - 1;
            int attacked = attack[1] - 1;

            System.out.println((attacker + 1) + " attacked " + (attacked + 1));

            if (set.find(attacker) == set.find(attacked)) {
                System.out.println("Ignored attack by " + (attacker + 1) + " on " + (attacked +1));
            } else {
                handleAttack(voters.get(attacker), voters.get(attacked));
            }
        }
    }

    /**
     * Handle individual attacks
     * @param attacker the attacker
     * @param attacked who the attacker is attacking
     */
    public void handleAttack(Voter attacker, Voter attacked) {
        if (attacker.prev != -1) set.union(attacked.val, attacker.prev);
        if (attacked.prev != -1) set.union(attacker.val, attacked.prev);

        attacker.prev = attacked.val;
        attacked.prev = attacker.val;

    }

    private class Voter {
        int val;
        int prev = -1;

        public Voter(int val) { this.val = val; }

        @Override
        public String toString() { return String.valueOf(val); }
    }
}
