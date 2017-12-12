from .. import Base
from sqlalchemy import Column, Integer, String, Table, ForeignKey
from sqlalchemy.orm import relationship

poi_tag_rel = Table('poi_tag_rel', Base.metadata,
	Column('poi_id', Integer, ForeignKey('poi.id')),
	Column('tag_id', Integer, ForeignKey('poi_tag.id'))
)

class POI(Base):
	__tablename__ = 'poi'

	id = Column(Integer, primary_key=True)
	building_id = Column(Integer)
	name = Column(String(256))
	url = Column(String(512))
	x = Column(Integer)
	y = Column(Integer)

	tags = relationship('POITag', secondary=poi_tag_rel, back_populates='pois')

	def __init__(self, building_id, name, url, x, y):
		self.building_id = building_id
		self.name = name
		self.url = url
		self.x = x
		self.y = y

	@property
	def serialize(self):
		return {
			'id': self.id,
			'building_id': self.building_id,
			'name': self.name,
			'url': self.url,
			'x': self.x,
			'y': self.y
		}

class POITag(Base):
	__tablename__ = 'poi_tag'

	id = Column(Integer, primary_key=True)
	name = Column(String(128))

	pois = relationship('POI', secondary=poi_tag_rel, back_populates='tags')

	def __init__(self, name):
		self.name = name
	
	@property
	def serialize(self):
		return {
			'id': self.id,
			'name': self.name
		}