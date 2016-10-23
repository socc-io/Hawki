import sys, json

def build(building_id='', source='../../../Data/WRM/RAW/', target='VOCAS/'):
    fullPath = source + building_id + '.dat'
    opf = open(fullPath).read().split("\n")
    opf.pop()
    building_set = {}
    for line in opf:
        row = json.loads(line)
        bset = building_set.get(building_id, {})
        for item in row.keys():
            if item not in ['x', 'y', 'z', 'bid']:
                for apinfo in row[item]:
                    bset[apinfo['bssid']] = bset.get(apinfo['bssid'], 0) + 1
        building_set[building_id] = bset

    for bid in building_set.keys():
        bset = building_set[bid]
        output = target + bid + ".voca"
        with open(output, 'w') as f:
            f.write(",".join(bset.keys()))

if __name__ == '__main__':
    if len(sys.argv) > 1:
        building_id = sys.argv[1]
        build(building_id)
    else:
        print('please run with argument: python SUPER_APVOCA.py testbuilding')
