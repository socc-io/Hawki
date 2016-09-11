import json

def build(path = ""):
    opf = open(path).read().split("\n")
    opf.pop()
    building_set = {}
    for line in opf:
        row = json.loads(line)
        building_id = row["building_id"]
        bset = building_set.get(building_id, {})
        for item in row.keys():
            if item not in ['building_id', 'x', 'y', 'z']:
                bset[item] = bset.get(item, 0) + 1
        building_set[building_id] = bset

    for bid in building_set.keys():
        bset = building_set[bid]
        print bset
        output = "Predictor/Module/APVOCA/VOCAS/" + bid + ".voca"
        outpf = open(output, 'w')
        outpf.write(",".join(bset.keys()) + "\n")
        outpf.close()

build(path = "Predictor/Module/APVOCA/test/test_raw.rssi")
