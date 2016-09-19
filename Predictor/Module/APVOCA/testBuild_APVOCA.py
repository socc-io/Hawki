import json

def build(path = ""):
    building_id = path.split('/')[-1]
    building_id = building_id.split('.')[0]

    opf = open(path).read().split("\n")
    opf.pop()
    building_set = {}
    for line in opf:
        row = json.loads(line)
        bset = building_set.get(building_id, {})
        for item in row.keys():
            if item not in ['x', 'y', 'z', 'bid']:
                for apinfo in row[item]:
                    bset[apinfo['sid']] = bset.get(apinfo['sid'], 0) + 1
        building_set[building_id] = bset

    for bid in building_set.keys():
        bset = building_set[bid]
        output = "VOCAS/" + bid + ".voca"
        with open(output, 'w') as f:
            f.write(",".join(bset.keys()))

build(path = "../../../Data/WRM/RAW/testbuilding.dat")
