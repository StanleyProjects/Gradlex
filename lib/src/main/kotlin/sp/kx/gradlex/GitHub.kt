package sp.kx.gradlex

import java.util.Objects

object GitHub {
    class Repository(val owner: String, val name: String) {
        init {
            require(owner.isNotBlank()) { "The owner is blank!" }
            require(name.isNotBlank()) { "The name is blank!" }
        }

        override fun toString(): String {
            return "Repository($owner/$name)"
        }

        override fun hashCode(): Int {
            return Objects.hash(owner, name)
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Repository) return false
            return owner == other.owner && name == other.name
        }
    }
}
