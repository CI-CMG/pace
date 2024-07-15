package edu.colorado.cires.pace.repository.search;

import edu.colorado.cires.pace.data.object.Contact;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public abstract class ContactSearchParameters<C extends Contact> extends ObjectWithNameSearchParameters<C> {

}
