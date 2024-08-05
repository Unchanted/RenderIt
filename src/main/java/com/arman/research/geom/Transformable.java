package com.arman.research.geom;

import com.arman.research.geom.transforms.Transform3f;
import com.arman.research.geom.vectors.Vector3f;

public interface Transformable {

    void add(Vector3f v);

    void subtract(Vector3f v);

    void add(Transform3f t);

    void subtract(Transform3f t);

    void addRotation(Transform3f t);

    void subtractRotation(Transform3f t);

}
