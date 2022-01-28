package entity;

import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Currency {
    USD(431), EUR(451), RUB(456);
    private final int id;
}
