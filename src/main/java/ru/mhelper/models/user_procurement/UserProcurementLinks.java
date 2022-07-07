package ru.mhelper.models.user_procurement;

import ru.mhelper.models.BaseEntity;
import ru.mhelper.models.procurements.Procurement;
import ru.mhelper.models.users.User;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_procurement_links")
public class UserProcurementLinks extends BaseEntity {
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "procurement_id", nullable = false)
    private Procurement procurement;

    public Procurement getProcurement() {
        return procurement;
    }

    public void setProcurement(Procurement procurement) {
        this.procurement = procurement;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProcurementLinks)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        UserProcurementLinks that = (UserProcurementLinks) o;

        if (!user.equals(that.user)) {
            return false;
        }
        return procurement.equals(that.procurement);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + procurement.hashCode();
        return result;
    }
}