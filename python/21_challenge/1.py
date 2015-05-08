def change(x, y, z, card):
    if (x > card or y > card or z > card):
        return False
    return z + y * card + x * pow(card, 2)

def judge(x, y, z, first_card, second_card):
    first = change(x, y, z, first_card)
    second = change(z, y, x, second_card)
    if first == False or second == False or first != second:
        return False
    return True

for i in range(10):
    for j in range(10):
        for k in range(10):
            if judge(i, j, k, 10, 9):
                print i, j, k
