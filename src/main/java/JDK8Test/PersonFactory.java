package JDK8Test;

/**
 * Created by YWJ on 2017.4.18.
 * Copyright (c) 2017 NJU PASA Lab All rights reserved.
 */
public interface PersonFactory<P extends Person> {
    P create(String a, String b);
}
