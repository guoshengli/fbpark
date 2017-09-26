package com.fbpark.rest.model;


import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class RecommandationId implements Serializable {
	private static final long serialVersionUID = -7020717905607738823L;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "poi_id")
	private Poi poi;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "reference_id")
	private Poi reference;

	public Poi getPoi() {
		return poi;
	}

	public void setPoi(Poi poi) {
		this.poi = poi;
	}

	public Poi getReference() {
		return reference;
	}

	public void setReference(Poi reference) {
		this.reference = reference;
	}

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof RecommandationId)) {
			return false;
		}
		RecommandationId otherFollowId = (RecommandationId) other;

		if ((otherFollowId.getPoi().getId() == getPoi().getId())
				&& (otherFollowId.getReference().getId() == getReference().getId())) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int result = getPoi().hashCode();
		result = 31 * result + getReference().hashCode();
		return result;
	}
}
