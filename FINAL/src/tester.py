try:
    print(["foo", "bar", "baz"].index("baz"))
except ValueError:
    print("WHOOPS")