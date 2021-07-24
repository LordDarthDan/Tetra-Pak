package mod.noobulus.tetrapak.util.tetra_definitions;


import java.util.Objects;

public class Pair<F, S> {
	F first;
	S second;

	protected Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	public static <F, S> Pair<F, S> of(F first, S second) {
		return new Pair<>(first, second);
	}

	public F getFirst() {
		return this.first;
	}

	public void setFirst(F first) {
		this.first = first;
	}

	public S getSecond() {
		return this.second;
	}

	public void setSecond(S second) {
		this.second = second;
	}

	public Pair<F, S> copy() {
		return of(this.first, this.second);
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		} else if (!(obj instanceof Pair)) {
			return false;
		} else {
			Pair<?, ?> other = (Pair<?, ?>) obj;
			return Objects.equals(this.first, other.first) && Objects.equals(this.second, other.second);
		}
	}

	public int hashCode() {
		return this.nullHash(this.first) * 31 ^ this.nullHash(this.second);
	}

	int nullHash(Object o) {
		return o == null ? 0 : o.hashCode();
	}

	public String toString() {
		return "(" + this.first + ", " + this.second + ")";
	}

	public Pair<S, F> swap() {
		return of(this.second, this.first);
	}
}
